package com.ypsx.event.service.register;

import com.alibaba.dubbo.config.*;
import com.ypsx.event.cache.ExecuteServiceCache;
import com.ypsx.event.service.handler.EventConsumerServiceHandler;
import com.ypsx.event.service.handler.ScheduleConsumerServiceHandler;
import com.ypsx.event.sevice.EventConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：抽象的服务注册者
 *
 * @author chuchengyi
 */
public abstract class AbstractConsumerServiceRegister {


    @Autowired
    protected RegistryConfig registryConfig;
    @Autowired
    protected ApplicationConfig applicationConfig;
    @Autowired
    protected ProtocolConfig protocol;

    @Resource(name="normalConsumerServiceHandler")
    protected EventConsumerServiceHandler normalConsumerServiceHandler;

    @Resource(name="scheduleConsumerServiceHandler")
    protected EventConsumerServiceHandler scheduleConsumerServiceHandler;
    /**
     * 功能：定义日志信息
     */
    protected final static Logger logger = LoggerFactory.getLogger("registerLog");

    /**
     * 功能：执行服务的缓存
     */
    protected ExecuteServiceCache executeServiceCache = ExecuteServiceCache.getInstance();





}
