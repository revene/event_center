package com.ypsx.event.listener;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;

/**
 * 事件消费监听器接口
 *
 * @author blanc
 */
public interface EventConsumerListener {

    /**
     * 要消费的事件类型
     *
     * @return eventType
     */
    String getEventType();

    /**
     * 消费事件的具体逻辑
     *
     * @param event 要消费事件
     * @return 消费事件的结果
     */
    EventResult consumerEvent(Event event);


}
