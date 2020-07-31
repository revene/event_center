package com.blanc.event.worker;

import com.blanc.event.cache.EventExecuteCache;
import com.blanc.event.cache.EventExecuteResultCache;
import com.blanc.event.model.Event;
import com.blanc.event.sevice.ConsumerService;
import com.blanc.event.sevice.EventConsumerService;
import com.blanc.event.sevice.EventScheduleConsumerService;
import com.google.common.base.Throwables;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.Serializable;

/**
 * 包装event, event的真实执行单元, Runnable接口
 *
 * @author wangbaoliang
 */
@Data
@Slf4j(topic = "eventLog")
public class EventExecuteTask implements Runnable, Serializable {

    private static final long serialVersionUID = -2437588070685164104L;

    /**
     * 功能：设置执行过程是否需要监控
     */
    private boolean monitor;

    /**
     * 待执行的event事件
     */
    private Event event;

    /**
     * 事件的类型,EventConsumerService or EventScheduleConsumerService
     */
    private ConsumerService service;

    /**
     * 事件执行的缓存
     */
    private EventExecuteCache eventExecuteCache;

    /**
     * 事件执行成功的缓存
     */
    private EventExecuteResultCache eventExecuteResultCache;

    /**
     * 用来进行系统检测
     */
    private StopWatch stopWatch;

    /**
     * 功能：构造任务执行的单元
     *
     * @param event
     * @param service
     * @param eventExecuteCache
     */
    public EventExecuteTask(Event event, ConsumerService service, EventExecuteCache eventExecuteCache, EventExecuteResultCache eventExecuteResultCache) {
        this.event = event;
        this.service = service;
        this.eventExecuteCache = eventExecuteCache;
        this.eventExecuteResultCache = eventExecuteResultCache;
        this.monitor = false;
    }

    /**
     * 功能：构造任务执行的单元
     *
     * @param event
     * @param service
     * @param eventExecuteCache
     * @param monitor
     */
    public EventExecuteTask(Event event, ConsumerService service, EventExecuteCache eventExecuteCache, EventExecuteResultCache eventExecuteResultCache, boolean monitor) {
        this.event = event;
        this.service = service;
        this.eventExecuteCache = eventExecuteCache;
        this.eventExecuteResultCache = eventExecuteResultCache;
        this.monitor = monitor;
        //当需要监控时候 设置停表对象
        if (this.monitor) {
            this.stopWatch = new StopWatch();
        }
    }

    @Override
    public void run() {
        try {
            //当已经执行成功了就直接跳过
            if (eventExecuteResultCache.isSuccessExecuted(event.getId())) {
                return;
            }
            //一段时间只能有一条记录成功
            if (eventExecuteCache.addExecute(event)) {
                //一台机器执行成功
                //开始进行监控 stopWatch
                startMonitor();
                //执行服务调用
                executeEvent(event);
                //结束监控 stopWatch
                endMonitor();
            }
        } catch (Throwable throwable) {
            log.error("EventExecuteTask[runs] is error bizId=" + event.getBizId(), throwable);
        }

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
    private void endMonitor() {
        if (this.monitor) {
            stopWatch.stop();
            String threadName = Thread.currentThread().getName();
            log.error(threadName + " id=" + event.getBizId() + " cost=" + stopWatch.getLastTaskTimeMillis());
        }
    }

    /**
     * 执行具体的事件
     *
     * @param event 待执行的事件
     */
    private void executeEvent(Event event) {
        try {
            //如果是普通事件
            if (service instanceof EventConsumerService) {
                EventConsumerService consumerService = (EventConsumerService) service;
                //立刻执行
                consumerService.consumerEvent(event);
            }
            //如果是定时事件
            if (service instanceof EventScheduleConsumerService) {
                EventScheduleConsumerService consumerService = (EventScheduleConsumerService) service;
                //立刻执行
                consumerService.consumerScheduleEvent(event);
            }
        } catch (Throwable throwable) {
            log.error("EventExecuteTask[executeEvent]  is error,caused by {}, event is {}",
                    Throwables.getStackTraceAsString(throwable),
                    event);
        }
    }
}
