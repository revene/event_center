package com.blanc.event.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.blanc.event.manager.EventLogManager;
import com.blanc.event.manager.EventManager;
import com.blanc.event.model.Result;
import com.blanc.event.service.EventScanService;
import com.blanc.event.sevice.EventPublishService;
import com.google.common.base.Throwables;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 发布事件的实现类
 *
 * @author wangbaoliang
 */
@Service(version = "1.0")
public class EventPublishServiceImpl implements EventPublishService {

    @Resource
    private EventManager eventManager;
    @Resource
    private EventLogManager eventLogManager;
    @Resource
    private EventScanService eventScanService;

    /**
     * 功能：时间间隔
     */
    private static final int TIME = 30 * 1000;

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @Override
    public Result<Long> publishEvent(Event event) {
        Result<Long> result = new Result<>();
        try {
            //保存事件信息
            result = eventManager.saveEvent(event);
            //获取到首次执行的时间
            Long firstExecuteTime = event.getExecuteTime();
            //当首次执行事件为不为空的时候 判断是否需要放入时间轮中去执行
            if (firstExecuteTime != null) {
                //获取当前时间
                Long now = System.currentTimeMillis();
                Long timeTick = firstExecuteTime - now;
                //提交事件到事件轮
                if (timeTick < TIME) {
                    eventScanService.submitEvent(event);
                }
            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventPublishServiceImpl[publishEvent] is error:" + throwable.getMessage());
        }
        return result;
    }

    @Override
    public Result cancelEvent(Event event) {
        return null;
    }

    @Override
    public Result successEvent(Event event) {
        return null;
    }

    @Override
    public Result reportEventExecute(Event event, EventResult eventResult) {
        Result result = new Result();
        try {
            eventLogManager.saveEventLog(eventResult, event);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventPublishServiceImpl[reportEventExecute] is error:" + throwable.getMessage());
        }
        return result;
    }


}
