package com.blanc.event.sevice;

import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;

/**
 * 普通事件执行器
 *
 * @author wangbaoliang
 */
public interface EventConsumerService extends ConsumerService {

    /**
     * 消费执行event
     *
     * @param event 事件
     * @return 消费结果
     */
    EventResult consumerEvent(Event event);
}
