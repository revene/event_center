package com.blanc.event.sevice;

import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;

/**
 * 消费定时任务类型的event
 *
 * @author wangbaoliang
 */
public interface EventScheduleConsumerService extends ConsumerService {


    /**
     * 功能：获取到事件发布服务
     *
     * @return
     */
    EventPublishService getEventPublishService();

    /**
     * 功能：设置事件发布服务
     *
     * @param eventPublishService
     */
    void setEventPublishService(EventPublishService eventPublishService);

    /**
     * 功能：消费事件消息
     *
     * @param event 要消费的事件
     * @return 消费事件的结果
     */
    EventResult consumerScheduleEvent(Event event);


}
