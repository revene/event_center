package com.blanc.event.sevice.impl;

import com.blanc.event.listener.EventConsumerListener;
import com.blanc.event.worker.EventScheduleReportWorker;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;
import com.blanc.event.model.ScheduleEventExecuteCache;
import com.blanc.event.sevice.AbstractEventConsumerService;
import com.blanc.event.sevice.EventPublishService;
import com.blanc.event.sevice.EventScheduleConsumerService;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 定时任务的执行实现类
 *
 * @author wangbaoliang
 */
public class EventScheduleServiceImpl extends AbstractEventConsumerService implements EventScheduleConsumerService {

    /**
     * 功能：停止当前线程
     */
    private Thread workerThread;

    /**
     * 功能：线程执行情况
     */
    private EventScheduleReportWorker reportWorker;

    /**
     * 功能：事件发布服务
     */
    private EventPublishService eventPublishService;

    /**
     * 功能：事件执行的缓存
     */
    private ScheduleEventExecuteCache eventExecuteCache = ScheduleEventExecuteCache.getInstance();

    /**
     * 无参构造器
     */
    public EventScheduleServiceImpl() {

    }

    public void init() {
        reportWorker = new EventScheduleReportWorker();
        reportWorker.setEventPublishService(eventPublishService);
        //构造监控线程
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //设置当前线程
        workerThread = threadFactory.newThread(reportWorker);
        workerThread.start();
    }


    @Override
    public EventResult consumerScheduleEvent(Event event) {
        EventResult result = new EventResult();
        //currentDate 为当前时间
        Date currentDate = new Date();
        try {
            //获取事件类
            String eventType = event.getEventType();
            //设置执行的ip信息 就是事件中心的本机ip
            result.setClientIp(address.getHostAddress());
            //获取处理器
            if (this.listenerMap.containsKey(eventType)) {
                //获取事件的处理机制
                EventConsumerListener listener = listenerMap.get(eventType);
                result = listener.consumerEvent(event);
            }
            //当没有事件处理器
            else {
                result.setSuccess(false);
                result.setErrorMessage("no Listener ");
            }
            result.setClientIp(address.getHostAddress());
        } catch (Throwable throwable) {
            result.setClientIp(address.getHostAddress());
            result.setErrorMessage(throwable.getMessage());
        } finally {
            Date endTime = new Date();
            //设置开始时间
            result.setStartExecute(currentDate);
            //设置结束时间
            result.setEndExecute(endTime);
            //将处理的结果返回给数据
            eventExecuteCache.addEventExecute(event, result);
        }
        return result;
    }

    @Override
    public EventPublishService getEventPublishService() {
        return eventPublishService;
    }

    @Override
    public void setEventPublishService(EventPublishService eventPublishService) {
        this.eventPublishService = eventPublishService;
    }

    @Override
    public void startConsumer() {
        if (eventPublishService == null) {
            throw new RuntimeException("EventScheduleServiceImpl init is error:eventPublishService=" + eventPublishService);
        } else {
            init();
        }

    }
}
