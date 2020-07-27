package com.blanc.event.worker;

import java.util.concurrent.BlockingQueue;

import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;
import com.blanc.event.model.ScheduleEventExecuteCache;
import com.blanc.event.sevice.EventPublishService;


/**
 * 功能：定时任务执行的情况的汇报
 *
 * @author chuchengyi
 */
public class EventScheduleReportWorker implements Runnable {


    /**
     * 功能：事件发布服务
     */
    private EventPublishService eventPublishService;

    /**
     * 功能：事件执行的缓存
     */
    ScheduleEventExecuteCache eventExecuteCache = ScheduleEventExecuteCache.getInstance();

    @Override
    public void run() {
        BlockingQueue<Event> eventQueue = eventExecuteCache.getEventQueue();
        while (true) {
            try {
                //阻塞等待执行的结果
                Event event = eventQueue.take();
                //汇报执行的结果
                reportExecute(event);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


    /**
     * 功能：汇报事件的执行结果
     *
     * @param event
     */
    private void reportExecute(Event event) {
        try {
            //获取到执行的结果
            EventResult eventResult = eventExecuteCache.getResult(event);
            //从缓存中移除
            eventExecuteCache.removeEventExecute(event);
            //汇报执行的情况
            eventPublishService.reportEventExecute(event, eventResult);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public EventPublishService getEventPublishService() {
        return eventPublishService;
    }

    public void setEventPublishService(EventPublishService eventPublishService) {
        this.eventPublishService = eventPublishService;
    }
}
