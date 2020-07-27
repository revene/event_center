package com.ypsx.event.service.handler;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.util.model.Result;

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
    public void process(Event event);

    /**
     * 功能：处理调用成功的消息返回
     *
     * @param result
     * @param event
     */

    public void processResult(EventResult result, Event event);


    /**
     * 功能：处理调用异常的情况处理
     *
     * @param ex
     * @param event
     */
    public void processException(Throwable ex, Event event);


}
