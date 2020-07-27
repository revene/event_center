package com.blanc.event.service.handler;

import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;

/**
 * 功能：远程服务调用异步处理
 *
 * @author chuchengyi
 */
public interface EventConsumerServiceHandler {


    /**
     * 功能：
     * @param event
     */
    void process(Event event);

    /**
     * 功能：处理调用成功的消息返回
     *
     * @param result
     * @param event
     */

    void processResult(EventResult result, Event event);


    /**
     * 功能：处理调用异常的情况处理
     *
     * @param ex
     * @param event
     */
    void processException(Throwable ex, Event event);


}
