package com.blanc.event.service.handler;

import com.blanc.event.cache.EventExecuteResultCache;
import com.blanc.event.cache.EventTypeCache;
import com.blanc.event.manager.EventLogManager;
import com.blanc.event.manager.EventManager;
import com.blanc.event.model.*;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * 功能：对象手工创建
 *
 * @author chuchengyi
 */
public class EventResultUpdater implements EventHandler<EventExecuteResult>, WorkHandler<EventExecuteResult> {


    /**
     * 功能：事件的逻辑处理类
     */
    private EventManager eventManager;


    /**
     * 功能：事件日志的处理类
     */
    private EventLogManager eventLogManager;


    /**
     * 功能：设置执行过程是否需要监控
     */
    private boolean monitor;

    /**
     * 功能：用来进行系统检测
     */
    private StopWatch stopWatch;

    /**
     * 功能：事件类型的缓存信息
     */
    private EventTypeCache eventTypeCache = EventTypeCache.getInstance();


    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    /**
     * 功能：缓存执行成功的缓存
     */
    private EventExecuteResultCache eventExecuteResultCache;

    /**
     * 功能：事件对象的更新器
     *
     * @param eventManager
     * @param eventLogManager
     */
    public EventResultUpdater(EventManager eventManager, EventLogManager eventLogManager, EventExecuteResultCache eventExecuteResultCache) {
        this.eventManager = eventManager;
        this.eventLogManager = eventLogManager;
        this.eventExecuteResultCache = eventExecuteResultCache;
        this.monitor = false;
    }

    public EventResultUpdater(EventManager eventManager, EventLogManager eventLogManager, EventExecuteResultCache eventExecuteResultCache, boolean monitor) {
        this.eventManager = eventManager;
        this.eventLogManager = eventLogManager;
        this.eventExecuteResultCache = eventExecuteResultCache;
        this.monitor = monitor;
        if (monitor) {
            stopWatch = new StopWatch();
        }
    }


    @Override
    public void onEvent(EventExecuteResult eventExecuteResult, long l, boolean b) throws Exception {
        processResult(eventExecuteResult);
    }

    @Override
    public void onEvent(EventExecuteResult eventExecuteResult) throws Exception {
        processResult(eventExecuteResult);
    }


    /**
     * 功能：处理事件执行的结果
     *
     * @param eventExecuteResult
     */
    private void processResult(EventExecuteResult eventExecuteResult) {
        try {
            Event event = eventExecuteResult.getEvent();
            EventResult result = eventExecuteResult.getResult();
            //当数据和结果都有的时候才进行处理
            if (event != null && result != null) {
                startMonitor();
                //执行成功了加缓存
                if (result.isSuccess()) {
                    eventExecuteResultCache.addSuccessExecuted(event.getId());
                }
                Event update = genUpdateEvent(result, event);
                eventManager.updateEvent(update);
                //保存日志信息
                eventLogManager.saveEventLog(result, event);
                endMonitor(event);
            } else {
                //打印错误日志信息
                logger.error("EventResultUpdater[processResult] is error  event=:" + event, " result=" + result);
            }
            //这一句千万不能少，少了会数据对象在ringBuffer中保持强引用，对象不能释放
            eventExecuteResult.clear();
        } catch (Throwable throwable) {
            logger.error("EventResultUpdater[processResult] is error :", throwable);
        }
    }


    /**
     * 功能：增加执行次数
     *
     * @param event
     * @return
     */
    private Long genExecuteSize(Event event) {
        if (event.getExecuteSize() == null) {
            event.setExecuteSize(0L);
        }
        return event.getExecuteSize() + 1;
    }

    /**
     * 功能：生产时间跟新的结果
     *
     * @param result
     * @param event
     */
    private Event genUpdateEvent(EventResult result, Event event) {
        //设置更新事件信息
        Event updateEvent = new Event();
        updateEvent.setId(event.getId());
        //设置业务ID用来做分库分表使用
        updateEvent.setBizId(event.getBizId());
        //生成下一次
        Long executeSize = genExecuteSize(event);
        //执行次数自动加
        updateEvent.setExecuteSize(executeSize);
        //执行成功
        if (result.isSuccess()) {
            updateEvent.setStatus(EventStatus.SUCCESS.getStatus());
        }
        //执行失败
        else {
            //当不要重试的时候 直接设置最终失败
            if (!result.isRetry()) {
                updateEvent.setStatus(EventStatus.FAIL.getStatus());
            } else {
                //获取的内容信息
                EventType typeInfo = eventTypeCache.getEventType(event.getAppCode(), event.getEventType());
                //当当前执行的次数已经大于最大的次数的时候设置为失败
                if (event.getExecuteSize() > typeInfo.getMaxExecuteSize()) {
                    updateEvent.setStatus(EventStatus.FAIL.getStatus());
                }
                //设置为可用状态
                else {
                    //获取下次执行时间
                    updateEvent.setExecuteTime(System.currentTimeMillis() + typeInfo.getScheduleTime());
                    updateEvent.setStatus(EventStatus.ACTIVE.getStatus());
                }
            }
        }
        return updateEvent;
    }


    /**
     * 功能：开始进行监控
     */
    private void startMonitor() {
        if (this.monitor) {
            stopWatch.start();
        }
    }

    /**
     * 功能：停止监控
     */
    private void endMonitor(Event event) {
        if (this.monitor) {
            stopWatch.stop();
            String threadName = Thread.currentThread().getName();
            logger.error(threadName + " id=" + event.getBizId() + " cost=" + stopWatch.getLastTaskTimeMillis());
        }
    }


}
