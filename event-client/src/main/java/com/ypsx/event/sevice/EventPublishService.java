package com.ypsx.event.sevice;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.util.model.Result;

import java.util.List;

/**
 * @author chuchengyi
 */
public interface EventPublishService {


    /**
     * 功能：发布事件
     *
     * @param event
     * @return
     */
    public Result<String> publishEvent(Event event);


    /**
     * 功能：取消事件
     *
     * @param event
     * @return
     */
    public Result<Boolean> cancelEvent(Event event);


    /**
     * 功能：成功事件
     *
     * @param event
     * @return
     */
    public Result<Boolean> successEvent(Event event);


    /**
     * 功能：汇报事件的执行情况
     *
     * @param event
     * @param eventResult
     * @return
     */
    public Result<Boolean> reportEventExecute(Event event, EventResult eventResult);


}
