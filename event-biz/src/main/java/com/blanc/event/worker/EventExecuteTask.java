package com.blanc.event.worker;

import com.blanc.event.cache.EventExecuteCache;
import com.blanc.event.cache.EventExecuteResultCache;
import com.blanc.event.model.Event;
import com.blanc.event.sevice.ConsumerService;
import com.blanc.event.sevice.EventConsumerService;
import com.blanc.event.sevice.EventScheduleConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.Serializable;

/**
 * 功能：任务执行的单元
 *
 * @author chuchengyi
 */
public class EventExecuteTask implements Runnable, Serializable {

    private static final long serialVersionUID = -2437588070685164104L;


    /**
     * 功能：设置执行过程是否需要监控
     */
    private boolean monitor;
    /**
     * 功能：事件的数据信息
     */
    private Event event;

    /**
     * 功能：事件的执行服务
     */
    private ConsumerService service;


    /**
     * 功能：事件执行的缓存
     */
    private EventExecuteCache eventExecuteCache;

    /**
     * 功能：事件执行成功的缓存
     */
    private EventExecuteResultCache eventExecuteResultCache;

    /**
     * 功能：用来进行系统检测
     */
    private StopWatch stopWatch;

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    public EventExecuteTask() {

    }


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
            logger.error("EventExecuteTask[runs] is error bizId=" + event.getBizId(), throwable);
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
            logger.error(threadName + " id=" + event.getBizId() + " cost=" + stopWatch.getLastTaskTimeMillis());
        }
    }


    /**
     * 功能：执行具体的任务信息
     *
     * @param event
     */
    private void executeEvent(Event event) {
        try {
            //功能：获取执行的服务信息
            if (service instanceof EventConsumerService) {
                EventConsumerService consumerService = (EventConsumerService) service;
                //立刻执行
                consumerService.consumerEvent(event);
            }
            if (service instanceof EventScheduleConsumerService) {
                EventScheduleConsumerService consumerService = (EventScheduleConsumerService) service;
                //立刻执行
                consumerService.consumerScheduleEvent(event);
            }
        } catch (Throwable throwable) {
            logger.error("EventExecuteTask[executeEvent]  is error event:" + event.toString(), throwable);
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ConsumerService getService() {
        return service;
    }

    public void setService(EventConsumerService service) {
        this.service = service;
    }
}
