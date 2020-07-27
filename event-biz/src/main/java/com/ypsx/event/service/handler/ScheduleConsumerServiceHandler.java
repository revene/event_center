package com.ypsx.event.service.handler;

import com.ypsx.event.cache.EventTypeCache;
import com.ypsx.event.manager.EventLogManager;
import com.ypsx.event.manager.EventManager;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.event.model.EventStatus;
import com.ypsx.event.model.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author chuchengyi
 */
@Component("scheduleConsumerServiceHandler")
public class ScheduleConsumerServiceHandler implements EventConsumerServiceHandler {

    @Resource
    private EventManager eventManager;

    @Resource
    private EventLogManager eventLogManager;

    /**
     * 功能：事件类型的缓存信息
     */
    private EventTypeCache eventTypeCache = EventTypeCache.getInstance();


    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    /**
     * 功能：执行的日志信息
     *
     * @param event
     */
    private void doUpdateEvent(Event event) {
        //设置更新事件信息
        Event updateEvent = new Event();
        updateEvent.setId(event.getId());
        //设置业务ID为空
        updateEvent.setBizId(event.getBizId());
        //设置执行次数+1
        updateEvent.setExecuteSize(event.getExecuteSize() + 1);
        //获取下次执行时间
        Long nextExecuteTime = getNextExecuteTime(event);
        updateEvent.setExecuteTime(nextExecuteTime);
        updateEvent.setStatus(EventStatus.ACTIVE.getStatus());
        eventManager.updateEvent(updateEvent);
    }


    /**
     * 功能：获取到下次执行时间
     *
     * @param event
     * @return
     */
    private long getNextExecuteTime(Event event) {
        //获取到事件的类型
        EventType typeInfo = eventTypeCache.getEventType(event.getAppCode(), event.getEventType());
        //获取到事件类型执行的表达式
        String cronExpression = typeInfo.getCronExpression();
        //构造cron表达式解析类
        CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
        //获取到上次执行的时间
        Long executeTime = System.currentTimeMillis();
        //设置当前时间
        Date now = new Date(executeTime);
        //获取到下次执行的时间
        Date nextTime = generator.next(now);
        return nextTime.getTime();
    }

    @Override
    public void process(Event event) {
        try {
            //跟新事件信息
            doUpdateEvent(event);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error("ScheduleConsumerServiceHandler[processResult] is error:" + throwable.getMessage());
        }
    }

    @Override
    public void processException(Throwable ex, Event event) {
        try {
            logger.error("ScheduleConsumerServiceHandler[processResult] is error ex:" + ex.getMessage());
            //跟新事件信息
            doUpdateEvent(event);
        } catch (Throwable throwable) {
            logger.error("ScheduleConsumerServiceHandler[processResult] is error throwable:" + throwable.getMessage());
        }
    }

    @Override
    public void processResult(EventResult result, Event event) {

    }


}
