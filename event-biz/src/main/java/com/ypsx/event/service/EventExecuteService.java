package com.ypsx.event.service;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.Result;
import com.ypsx.event.sevice.ConsumerService;

/**
 * 功能：事件执行服务
 *
 * @author chuchengyi
 */
public interface EventExecuteService {

    /**
     * 功能：初始化化服务只有在系统初始化的时候调用
     */
    public void initService();


    /**
     * 功能：根据事件的类型来获取调用服务的信息
     *
     * @param event
     * @return
     */
    public ConsumerService getService(Event event);


    /**
     * 功能：直接执行
     *
     * @param event
     * @return
     */
    public Result executeEvent(Event event);


    /**
     * 功能：提交到线程池执行
     *
     * @param event
     * @return
     */
    public Result submitEvent(Event event);
}
