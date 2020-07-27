package com.ypsx.event.sevice.impl;

import com.ypsx.event.listener.EventConsumerListener;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.event.sevice.AbstractEventConsumerService;
import com.ypsx.event.sevice.EventConsumerService;

import java.util.Date;

/**
 * 功能：事件处理服务的实现
 *
 * @author chuchengyi
 */
public class EventConsumerServiceImpl extends AbstractEventConsumerService implements EventConsumerService {


    @Override
    public EventResult consumerEvent(Event event) {
        EventResult result = new EventResult();

        Date startTime = new Date();
        try {
            //设置执行的ip信息
            result.setClientIp(address.getHostAddress());
            //获取事件类
            String eventType = event.getEventType();
            //获取处理器
            if (this.listenerMap.containsKey(eventType)) {
                EventConsumerListener listener = this.listenerMap.get(eventType);
                result = listener.consumerEvent(event);
            }
            //当没有事件处理器
            else {
                result.setSuccess(false);
                result.setErrorMessage("no Listener ");
            }
        } catch (Throwable throwable) {
            result.setErrorMessage(throwable.getMessage());
        } finally {
            Date endTime = new Date();
            //设置开始时间
            result.setStartExecute(startTime);
            //设置结束时间
            result.setEndExecute(endTime);

        }
        return result;
    }


    @Override
    public void startConsumer() {

    }
}
