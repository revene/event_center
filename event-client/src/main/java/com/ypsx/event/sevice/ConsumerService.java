package com.ypsx.event.sevice;

import com.ypsx.event.listener.EventConsumerListener;

/**
 * @author chuchengyi
 */
public interface ConsumerService {

    /**
     * 功能：添加事件消费接口
     *
     * @param listener
     */
    public void addListener(EventConsumerListener listener);


    /**
     * 功能：启动消费处理器
     */
    public void startConsumer();
}
