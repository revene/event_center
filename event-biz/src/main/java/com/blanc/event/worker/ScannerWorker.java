package com.blanc.event.worker;

import com.blanc.event.manager.EventScanManager;
import com.blanc.event.model.Result;
import com.blanc.event.service.EventScanService;
import com.blanc.event.entity.EventQuery;
import com.blanc.event.manager.EventScanNodeManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventScanNode;
import com.blanc.event.model.EventStatus;
import com.blanc.event.timer.WorkerStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 扫描事件的工作线程
 *
 * @author wangbaoliang
 */
@Data
@Slf4j(topic = "eventLog")
public class ScannerWorker implements Runnable {

    /**
     * 当前线程的状态
     */
    private volatile int status;

    /**
     * 功能：当前线程状态描述
     */
    private static final String STATUS = "status";

    /**
     * 功能：状态属性的修改器
     */
    private static final AtomicIntegerFieldUpdater<ScannerWorker> STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(ScannerWorker.class, STATUS);

    /**
     * 扫描的节点,一个节点scanNode对应一个scanWorkerx
     */
    private EventScanNode eventScanNode;

    /**
     * 事件扫描服务类,里面只有一个功能,就是扫描事件
     */
    private EventScanManager eventScanManager;

    /**
     * 事件扫描节点服务,里面就一个功能,就是列举出所有的扫描节点
     */
    private EventScanNodeManager eventScanNodeManager;

    /**
     * 功能：事件处理器
     */
    private EventTaskListener eventTaskListener;

    /**
     * 功能：事件扫描服务
     */
    private EventScanService eventScanService;

    /**
     * 功能：定义启动重试的次数
     */
    private static final int START_RETRY = 3;

    /**
     * 功能：定义额外扫描的时间
     */
    private static final int TIME_ADD = 10 * 1000;

    /**
     * 功能：获取到默认扫描的数据
     */
    private static final int DEFAULT_SCAN_SIZE = 50;

    /**
     * 功能：定义扫描回溯的周期 过了扫描时间还有数据没有扫描 整个时间周期为 LOOP_SCAN_BACK_SIZE*TIME_ADD
     */
    private static final int LOOP_SCAN_BACK_SIZE = 10;

    /**
     * 功能：获取时间定时器
     */
    private StopWatch stopWatch = new StopWatch();

    /**
     * 无参构造器
     */
    public ScannerWorker() {
        //初始化状态
        this.status = WorkerStatus.INIT.getStatus();
    }

    /**
     * 功能:构造事件
     *
     * @param eventScanNode 事件扫描节点
     */
    public ScannerWorker(EventScanNode eventScanNode) {
        //scanWorker中持有了scanNode
        this.eventScanNode = eventScanNode;
        //设置为初始化的状态
        this.status = WorkerStatus.INIT.getStatus();
    }

    /**
     * scanWorker的核心逻辑,实际启动开始跑事件的扫描了
     */
    @Override
    public void run() {
        /**
         * 设置启动线程 就是修改ScanWork的状态，
         */
        doStart();
        int loopSize = 0;
        //start状态下才loopScan
        while (isStart()) {
            doLoopScan();
            loopSize++;
            //每过制定的周期进行回溯扫描，防止有漏数据的情况 10次一回头
            if (loopSize % LOOP_SCAN_BACK_SIZE == 0) {
                //初始化需要执行数据的时间为0，从0开始扫，确保不会漏扫，但是？前面的移动时间，没有看出
                eventScanNode.setScanTime(0L);
                //重新执行一次扫描
                doLoopScan();
            }
        }
    }


    /**
     * 功能：周期性的扫描表任务表里面的数据 发送到时间轮
     */
    private void doLoopScan() {
        try {
            //开始计时
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            stopWatch.start();
            //这里是0,设置事件执行的过滤条件，扫描事件的过滤条件，所有扫描到的事件的下次执行事件均要在startTime之后，数据库中startTime为0，说明所有的都会扫到
            final long startTime = eventScanNode.getScanTime();
            //设置事件执行的过滤条件，事件的执行时间在当前时间10s之内都会被扫描到，那些执行事件已经过期的也会被扫描到
            long endTime = System.currentTimeMillis() + TIME_ADD;
            //获取表索引信息，就是event_scan中的表索引 (1,2,3,4)，这个tableIndex用于做sharding的hint算法
            final int hintTableIndex = eventScanNode.getTableIndex();
            //分页查询limit的设置，50一页
            int dataSize = DEFAULT_SCAN_SIZE;
            //初始化分页查询的偏移量
            long offet = 0;
            //循环分页查询
            while (dataSize == DEFAULT_SCAN_SIZE) {
                //扫描数据 offset从0 开始 50分页
                Result<List<Event>> scanResult = eventScanManager.scanList(
                        buildEventQuery(EventStatus.ACTIVE.getStatus(), startTime, endTime, offet), hintTableIndex);
                if (scanResult.isSuccess()) {
                    //偏移量向后
                    offet += DEFAULT_SCAN_SIZE;
                    //获取数据列表
                    List<Event> dataList = scanResult.getData();
                    //获取查询出的结果,如果没有limit的数量，说明扫到尾了,及时漏扫了，下次也能补回来
                    dataSize = dataList.size();
                    //打印日志，输出hint规格，数据索引，以及扫描行数
                    log.info("ScannerWorker[doLoopScan] scan data tableIndex is {}, dataIndex is {}, dataSize is {}", hintTableIndex, offet, dataSize);
                    for (Event event : dataList) {
                        addTask(event);
                        String startDate = simpleDateFormat.format(startTime);
                        String endDate = simpleDateFormat.format(endTime);
                        log.info("scan and add event to Task,tableIndex is {} , event is {} , startTime is {} , endTime is {}",
                                hintTableIndex, event, startDate, endDate);
                    }
                }
            }
            //计时结束
            stopWatch.stop();
            //用stopWatch获取任务的执行时间
            long costTime = stopWatch.getLastTaskTimeMillis();
            //如果花费的时间大于了每次扫描的executeTime的时间间隔（这里是10s），即如果扫描的时间大于10s，则
            if (costTime > TIME_ADD) {
                //todo 没有懂这里是什么意思，把scanTime往前移动一段距离，每次scan的范围都会有部分重复
                endTime = endTime - (costTime - TIME_ADD);
            }
            //设置当前结束时间为下次开始的时间
            eventScanNode.setScanTime(endTime);
            //如果当costTime小于ADD_TIME的时候，才sleep一段时间
            sleepTick(costTime);
        } catch (Throwable throwable) {
            log.error("ScannerWorker[doLoopScan] is error" + throwable.getMessage());
        }
    }

    /**
     * 构建事件查询对象
     *
     * @param eventStatus 事件状态
     * @param startTime   事件查询时间范围:起始时间
     * @param endTime     事件查询时间范围:结束时间
     * @param offset      分页偏移量
     * @return
     */
    private EventQuery buildEventQuery(int eventStatus, long startTime, long endTime, long offset) {
        EventQuery eventQuery = new EventQuery();
        eventQuery.setStatus(eventStatus);
        eventQuery.setStartTime(startTime);
        eventQuery.setEndTime(endTime);
        eventQuery.setOffset(offset);
        eventQuery.setLimit(ScannerWorker.DEFAULT_SCAN_SIZE);
        return eventQuery;
    }

    /**
     * 功能：稍微做个停顿
     *
     * @param millis
     */
    private void sleepTick(long millis) {
        try {
            //如果costTime小于timeADD （10s），则睡眠掉costTime
            millis = TIME_ADD - millis;
            if (millis > 0) {
                Thread.sleep(millis);
            }
        } catch (Throwable throwable) {
            log.error("ScannerWorker[sleepTick] is error" + throwable.getMessage());
        }
    }


    /**
     * 功能：直接睡眠一段时间
     *
     * @param millis
     */
    private void sleepTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable throwable) {
            log.error("ScannerWorker[sleepTime] is error" + throwable.getMessage());
        }
    }

    /**
     * dataIndex是替代雪花算法的自增主键,不然分页查询的顺序没有办法保证?为什么不直接用主键id
     *
     * @param event     当前的事件
     * @param dataIndex 已有的最大的index
     * @return
     */
    private long getMaxDataIndex(Event event, long dataIndex) {
        long result;
        if (event.getDataIndex() > dataIndex) {
            result = event.getDataIndex();
        } else {
            result = dataIndex;
        }
        return result;
    }

    /**
     * 功能：添加任务
     *
     * @param event
     */
    private void addTask(Event event) {
        try {
            //将event包装成
            Result result = this.eventScanService.submitEvent(event);
            if (!result.isSuccess()) {
                log.error("ScannerWorker[addTask] is error" + result.getErrorMessage());
                //停顿个1s让时间轮有个缓冲防止内存爆掉
                sleepTime(1000);
            }
        } catch (Throwable throwable) {
            log.error("ScannerWorker[addTask] is error" + throwable.getMessage());
        }

    }


    /**
     * 功能：判断scannerWorker是否已经处于启动的状态
     *
     * @return is or not
     */
    public boolean isStart() {
        //获取到开始状态
        int started = WorkerStatus.STARTED.getStatus();
        //判断是不是已经启动过了
        if (STATE_UPDATER.get(this) == started) {
            return true;
        }
        return false;
    }


    /**
     * 功能：设施线程到启动状态
     *
     * @return true返回启动成功 false返回启动失败
     */
    public boolean doStart() {
        //获取到开始状态
        int started = WorkerStatus.STARTED.getStatus();
        //获取到结束状态
        int init = WorkerStatus.INIT.getStatus();
        //已经启动了之后直接返回
        if (isStart()) {
            return true;
        } else {
            //尝试初始化三次
            for (int i = 0; i < START_RETRY; i++) {
                //将线程状态更新停止状态 acs自旋做状态更新
                if (STATE_UPDATER.compareAndSet(this, init, started)) {
                    return true;
                }
            }
        }
        //打印scanWork是否启动
        log.info(Thread.currentThread().getName() + "is start ：" + isStart());
        //判断是否启动完成
        return isStart();
    }

    /**
     * 功能：停止线程
     *
     * @return
     */
    public boolean stop() {
        //获取到开始状态
        int started = WorkerStatus.STARTED.getStatus();
        //获取到结束状态
        int shutdown = WorkerStatus.SHUTDOWN.getStatus();

        //根据acs算法将线程状态更新停止状态，如果更新成功了，则直接返回true
        if (STATE_UPDATER.compareAndSet(this, started, shutdown)) {
            return true;
        }
        //判断当前线程是否已经是停止状态
        else {
            //如果已经是停止状态直接返回true
            if (STATE_UPDATER.getAndSet(this, shutdown) == shutdown) {
                return true;
            }
            //返回false
            else {
                return false;
            }
        }
    }
}
