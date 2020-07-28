package com.blanc.event.sevice;

import com.blanc.event.model.Result;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;

/**
 * 事件发布服务
 *
 * @author wangbaoliang
 */
public interface EventPublishService {


    /**
     * 功能：发布事件
     *
     * @param event 要发布的事件
     * @return
     */
    Result<Long> publishEvent(Event event);


    /**
     * 功能：取消事件
     *
     * @param event
     * @return
     */
    Result cancelEvent(Event event);


    /**
     * 功能：成功事件
     *
     * @param event
     * @return
     */
    Result successEvent(Event event);


    /**
     * 功能：汇报事件的执行情况
     *
     * @param event
     * @param eventResult
     * @return
     */
    Result reportEventExecute(Event event, EventResult eventResult);


}
