package com.ypsx.event.sevice;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;

/**
 * 功能：专门为定时任务去设计的解决是长时间定时任务的接口
 *
 * @author chuchengyi
 */
public interface EventScheduleConsumerService extends ConsumerService {


    /**
     * 功能：获取到事件发布服务
     *
     * @return
     */
    public EventPublishService getEventPublishService();

    /**
     * 功能：设置事件发布服务
     *
     * @param eventPublishService
     */
    public void setEventPublishService(EventPublishService eventPublishService);

    /**
     * 功能：消费事件消息
     *
     * @param event
     * @return
     */
    public EventResult consumerScheduleEvent(Event event);



}
