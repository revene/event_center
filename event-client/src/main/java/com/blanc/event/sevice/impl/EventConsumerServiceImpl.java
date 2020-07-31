package com.blanc.event.sevice.impl;

import com.blanc.event.listener.EventConsumerListener;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;
import com.blanc.event.sevice.AbstractEventConsumerService;
import com.blanc.event.sevice.EventConsumerService;

import java.util.Date;

/**
 * 事件处理
 *
 * @author wangbaoliang
 */
public class EventConsumerServiceImpl extends AbstractEventConsumerService implements EventConsumerService {


    @Override
    public EventResult consumerEvent(Event event) {
        EventResult result = new EventResult();
        Date currentDate = new Date();
        try {
            //设置执行的ip信息
            result.setClientIp(address.getHostAddress());
            //获取事件类
            String eventType = event.getEventType();
            //获取处理器
            if (this.listenerMap.containsKey(eventType)) {
                EventConsumerListener listener = this.listenerMap.get(eventType);
                result = listener.consumerEvent(event);
            } else {
                result.setSuccess(false);
                result.setErrorMessage("no Listener ");
            }
        } catch (Throwable throwable) {
            result.setErrorMessage(throwable.getMessage());
        } finally {
            Date endTime = new Date();
            //设置开始时间
            result.setStartExecute(currentDate);
            //设置结束时间
            result.setEndExecute(endTime);

        }
        return result;
    }


    @Override
    public void startConsumer() {

    }
}
