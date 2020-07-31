package com.blanc.event.service;

import com.blanc.event.model.Result;
import com.blanc.event.model.Event;
import com.blanc.event.sevice.ConsumerService;

/**
 * 时间执行服务
 *
 * @author wangbaoliang
 */
public interface EventExecuteService {

    /**
     * 功能：初始化化服务只有在系统初始化的时候调用
     */
    void initService();


    /**
     * 功能：根据事件的类型来获取调用服务的信息
     *
     * @param event
     * @return
     */
    ConsumerService getService(Event event);


    /**
     * 直接执行event
     *
     * @param event 待执行事件
     * @return 执行result
     */
    Result executeEvent(Event event);


    /**
     * 提交到线程池执行event
     *
     * @param event 待执行事件
     * @return 执行result
     */
    Result submitEvent(Event event);
}
