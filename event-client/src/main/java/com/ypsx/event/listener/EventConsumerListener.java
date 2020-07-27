package com.ypsx.event.listener;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;

/**
 * @author chuchengyi
 */
public interface EventConsumerListener {


    /**
     * 功能：获取到事件类型
     *
     * @return
     */
    public String getEventType();


    /**
     * 功能：小费具体的事件信息
     *
     * @param event
     * @return
     */
    public EventResult consumerEvent(Event event);


}
