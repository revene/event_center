package com.blanc.event.manager.impl;

import com.alibaba.fastjson.JSON;
import com.blanc.event.model.*;
import com.google.common.base.Throwables;
import com.blanc.event.cache.EventTypeCache;
import com.blanc.event.dao.EventDao;
import com.blanc.event.error.ExceptionConstant;
import com.blanc.event.error.ExceptionUtil;
import com.blanc.event.manager.EventManager;
import com.ypsx.event.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 事件服务实现类
 *
 * @author wangbaoliang
 */
@Component
public class EventManagerImpl implements EventManager {


    @Resource
    private EventDao eventDao;

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");



    @Override
    public Result<String> saveEvent(Event event) {

        Result<String> result = new Result<String>();
        try {
            //检测输入信息是是否正确
//            ExceptionInfo exceptionCode = checkParam(event);
//            if (exceptionCode != ExceptionConstant.OK) {
//                ExceptionUtil.setException(result, exceptionCode);
//                return result;
//            }
            //获取到事件类型
            EventType eventType = EventTypeCache.getInstance().getEventType(event);
            if (eventType == null) {
                result.fail(ExceptionConstant.EVENT_TYPE_IS_NOT_EXIST.getExceptionMessage());
                ExceptionUtil.setException(result, ExceptionConstant.EVENT_TYPE_IS_NOT_EXIST);
                return result;
            }

            //判断事件是否已经存在
            Event exit = isExistEvent(event);
            if (exit != null) {
                //当已经存在直接返回true
                result.success();
                return result;
            } else {
                //重置执行的信息
                setEventInfo(event, eventType);
                //保存事件信息
                doSaveEvent(event);
                result.success(event.getId() + "");
            }

        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventManagerImpl[saveEvent] is error data=" + JSON.toJSONString(event) + "error:", throwable);
            throwable.printStackTrace();
        }
        return result;
    }


    /**
     * 功能：初始化事件执行需要的信息
     *
     * @param event
     * @param eventType
     */
    private void setEventInfo(Event event, EventType eventType) {

        event.setExecuteSize(0L);
        //设置创建时间
        event.setGmtCreate(new Date());
        //设置修改时间
        event.setGmtModify(new Date());
        //设置初始时间
        event.setStatus(EventStatus.ACTIVE.getStatus());
        //设置版本号
        if (event.getVersion() == null) {
            event.setVersion(0L);
        }
        //设置id
//        event.setId(ipKeyGenerator.generateKey().longValue());
        //获取第一次期望执行的时间
        Long expireTime = System.currentTimeMillis();
        if (eventType.getSchedule() == 0) {
            //加上轮休的时间
            expireTime = expireTime + eventType.getScheduleTime();
        }
        //设置执行时间
        if (event.getExecuteTime() == null || (event.getExecuteTime() < expireTime)) {
            event.setExecuteTime(expireTime);
        }
    }


    /**
     * 功能：判断事件信息是否存在
     *
     * @param event
     * @return
     */
    public Event isExistEvent(Event event) {
        String appCode = event.getAppCode();
        String eventType = event.getEventType();
        String bizId = event.getBizId();
        Long version = event.getVersion();
        if (version == null) {
            version = 0L;
        }
        return this.getEvent(appCode, eventType, bizId, version);
    }


    public Event getEvent(String appCode, String eventType, String bizId, Long version) {
        //必须手动初始化hint
//        HintManager hintManager = null;
        try {
//            hintManager = initHint(EVENT_TABLE, bizId, bizId);
            return eventDao.getEvent(appCode, eventType, bizId, version);
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
//            closeHint(hintManager);
        }

    }

    /**
     * 功能:向数据库插入事件信息
     *
     * @param event
     */
    private void doSaveEvent(Event event) {
//        HintManager hintManager = null;
        try {
            //必须手动初始化hint
            String bizId = event.getBizId();
//            hintManager = initHint(EVENT_TABLE, bizId, bizId);
            eventDao.insertEvent(event);
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
//            closeHint(hintManager);
        }

    }


    @Override
    public Result updateEvent(Event event) {
        Result result = new Result();
//        HintManager hintManager = null;
        try {
            String bizId = event.getBizId();
//            hintManager = initHint(EVENT_TABLE, bizId, bizId);
            eventDao.updateEvent(event);
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventManagerImpl[updateEvent] is error data=" + JSON.toJSONString(event) + "error:" + throwable.getMessage());
        } finally {
//            closeHint(hintManager);
        }
        return result;
    }

    @Override
    public Result<List<Event>> listEvent(EventQuery query) {
        Result<List<Event>> result = new Result<>();
//        HintManager hintManager = null;
        try {
//            hintManager = initHint(EVENT_TABLE, query.getBizId(), query.getBizId());
            List<Event> dataList = eventDao.listEvent(query);
            result.success(dataList);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventManagerImpl[updateEvent] is error data=" + JSON.toJSONString(query) + "error:" + throwable.getMessage());
        } finally {
//            closeHint(hintManager);
        }
        return result;
    }


    @Override
    public Result<Integer> countEvent(EventQuery query) {
        Result<Integer> result = new Result<>();
//        HintManager hintManager = null;
        try {
//            hintManager = initHint(EVENT_TABLE, query.getBizId(), query.getBizId());
            int dataSize = eventDao.countEvent(query);
            result.success(dataSize);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventManagerImpl[countEvent] is error data=" + JSON.toJSONString(query) + "error:" + throwable.getMessage());
        } finally {
//            closeHint(hintManager);
        }
        return result;
    }
}
