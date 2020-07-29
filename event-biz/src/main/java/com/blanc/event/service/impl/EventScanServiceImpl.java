package com.blanc.event.service.impl;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.blanc.event.manager.EventScanManager;
import com.blanc.event.manager.EventScanNodeManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventScanNode;
import com.blanc.event.model.Result;
import com.blanc.event.service.EventScanService;
import com.blanc.event.timer.Timer;
import com.blanc.event.timer.impl.HashedWheelTimer;
import com.blanc.event.worker.EventTaskListener;
import com.blanc.event.worker.EventTimerTask;
import com.blanc.event.worker.EventScanWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 时间扫描服务实现类
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "eventLog")
@Component
public class EventScanServiceImpl implements EventScanService {


    /**
     * 功能：初始化线程数
     */
    private static final int INIT_THREAD_SIZE = 8;

    /**
     * 功能:最大线程数
     */
    private static final int MAX_THREAD_SIZE = 32;

    /**
     * 功能：线程初始化时间
     */
    private static final long KEEP_LIVE_TIME = 0;

    /**
     * 功能：默认的延迟为30ms
     */
    private static final long DEFAULT_DELAY_TIME = 300;

    /**
     * 功能：线程池
     */
    private ExecutorService threadPool;

    @Autowired
    private EventScanManager eventScanManager;
    @Autowired
    private EventScanNodeManager eventScanNodeManager;
    @Autowired
    private EventTaskListener eventTaskListener;

    /**
     * 工作节点对应的工作线程
     * key:
     * value: 扫描事件的工作线程
     */
    private Map<Long, EventScanWorker> workerMap = new ConcurrentHashMap<>();

    /**
     * 功能：设置时间轮
     */
    private final Timer timer = new HashedWheelTimer();

    /**
     * eventScanServiceImpl的构造器
     */
    public EventScanServiceImpl() {
        //设置一个阻塞队列用于放Runnable,这个是无界队列,有撑爆内存的风险
        //todo 这里需要切换为有界队列,具体的选型后面定
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue(MAX_THREAD_SIZE);
        //线程工厂用与自定义线程的名称,翻译为,扫描事件的线程
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("scan Thread -%d").build();
        //设置线程池 最小8 最大32 空闲时间0, 无界队列, 无拒绝策略
        this.threadPool = new ThreadPoolExecutor(INIT_THREAD_SIZE, MAX_THREAD_SIZE, KEEP_LIVE_TIME,
                TimeUnit.MILLISECONDS, queue, factory);
    }


    /**
     * 将事件提交到时间轮
     *
     * @param event 要提交的事件
     * @return success or fail
     */
    @Override
    public Result submitEvent(Event event) {
        Result result = new Result();
        try {
            EventTimerTask task = new EventTimerTask();
            //当数据执行时间已经过期化 300ms内执行
            long delay = event.getExecuteTime() - System.currentTimeMillis();
            //如果执行时间在当前时间之前，说明这个已经过期了，设置delay为300ms
            if (delay < 0) {
                delay = DEFAULT_DELAY_TIME;
            }
            task.setEvent(event);
            //设置事件监听的处理器，其中包括execute方法
            task.setTaskListener(this.eventTaskListener);
            //将事件包装好提交到时间轮
            timer.newTimeout(task, delay, TimeUnit.MILLISECONDS);
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("ScannerWorker[submitEvent] is error" + throwable.getMessage());
        }
        return result;

    }

    /**
     * 节点开始扫描事件
     *
     * @param node 事件扫描节点
     * @return 扫描结果
     */
    @Override
    public Result scanEvent(EventScanNode node) {
        Result result = new Result();
        try {
            /**
             * 判断当前线程池中有没有已经工作的线程，
             * 如果没有的话直接创建,目前生产是一台机器有4个节点
             * 外部的cron表达式如果再执行scanEvent,节点已经在缓存中,表明之前启动过,就直接return了
             * 每一个scanNode对应一个scanWorker,所以一个机器有4个scanWorker
             * 每个scanwork 负责分库分表中的一个表的扫描
             */
            if (!workerMap.containsKey(node.getId())) {
                //构造扫描event的工作线程,并且设置对应的节点
                EventScanWorker worker = new EventScanWorker(node);
                //设置事件扫描服务 里面有线程池等等
                worker.setEventScanService(this);
                //设置事件扫描器 用来扫描event
                worker.setEventScanManager(this.eventScanManager);
                //设置节点扫描器,用于扫描节点
                worker.setEventScanNodeManager(this.eventScanNodeManager);
                //设置事件监听器,用于提交事件并执行
                worker.setEventTaskListener(this.eventTaskListener);
                //将数据节点放到工作线程的缓存中
                workerMap.put(node.getId(), worker);
                //线程池中开始执行worker的扫描任务
//                threadPool.submit(worker);
            }
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventScanServiceImpl[scanEvent] is error, caused by {}", Throwables.getStackTraceAsString(throwable));
        }
        return result;
    }

    /**
     * 取消本机器对事件的扫描
     *
     * @param node 扫描节点
     * @return success or fail
     */
    @Override
    public Result cancelScanEvent(EventScanNode node) {
        Result result = new Result<>();
        try {
            //获取节点的主键id
            Long id = node.getId();
            //如果workMap中包含了这个ScanWork
            if (workerMap.containsKey(id)) {
                //获取到这个ScanWork
                EventScanWorker worker = workerMap.get(id);
                //功能 停止工作
                worker.stop();
                //从当前工作线程组中移除
                workerMap.remove(id);
            }
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventScanServiceImpl[cancelScanEvent] is error, caused by {}", Throwables.getStackTraceAsString(throwable));
        }
        return result;
    }

    @Override
    public Result<Boolean> stop() {
        Result<Boolean> result = new Result<>();
        try {
            timer.stop();
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventScanServiceImpl[stop] is error, caused by {}", Throwables.getStackTraceAsString(throwable));

        }
        return result;
    }
}
