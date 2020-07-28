package com.blanc.event.manager.impl;

import com.blanc.event.cache.EventExecuteLogCache;
import com.blanc.event.dao.EventLogDao;
import com.blanc.event.manager.EventLogManager;
import com.blanc.event.model.*;
import com.blanc.event.util.IpUtil;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 功能：事件日志保存的处理机制
 *
 * @author chuchengyi
 */
@Component
public class EventLogManagerImpl implements EventLogManager {

    @Resource
    private EventLogDao eventLogDao;
    @Resource
    private EventExecuteLogCache eventExecuteLogCache;

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    @Override
    public Result<Boolean> saveEventLog(EventLog eventLog) {
        Result<Boolean> result = new Result<Boolean>();
//        HintManager hintManager = null;
        try {
//            //判断是否存在
//            if (!eventExecuteLogCache.isExecuteLog(eventLog)) {
//                eventLog.setId(ipKeyGenerator.generateKey().longValue());
//                eventLog.setGmtCreate(new Date());
//                eventLog.setGmtModify(new Date());
//                hintManager = initHint(LOG_TABLE, eventLog.getBizId(), eventLog.getBizId());
//                eventLogDao.insertEventLog(eventLog);
//                eventExecuteLogCache.addExecuteLog(eventLog);
//                result.setModel(true);
//                result.setSuccess(true);
//            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventLogManagerImpl[saveEventLog] is error ：" + throwable.getMessage());
        } finally {
//            hintManager.close();
        }
        return result;
    }


    @Override
    public Result<Boolean> saveEventLog(EventResult result, Event event) {
        Result<Boolean> saveResult = new Result<Boolean>();
        try {
            EventLog eventLog = new EventLog();
            BeanUtils.copyProperties(event, eventLog);
            //设置事件ID
            eventLog.setEventId(event.getId());
            eventLog.setBizId(event.getBizId());
            //设置执行时间
            eventLog.setExecuteTime(new Date());
            //设置执行的次数
            eventLog.setExecuteIndex((int) (event.getExecuteSize() + 1));
            //设置执行的客户端ip
            if (StringUtils.isEmpty(result.getClientIp())) {
                eventLog.setExecuteIp(IpUtil.UN_KNOWN_HOST);
            } else {
                eventLog.setExecuteIp(result.getClientIp());
            }
            //设置成功状态标志
            int success = result.isSuccess() ? 0 : -1;
            eventLog.setSuccess(success);
            //设置是否成功的标识
            eventLog.setErrorMessage(result.getErrorMessage());
            eventLog.setStartExecute(result.getStartExecute());
            eventLog.setEndExecute(result.getEndExecute());
            //当开始时间和结束时间都有的时候 获取耗时
            if (eventLog.getEndExecute() != null && eventLog.getStartExecute() != null) {
                Long costTime = eventLog.getEndExecute().getTime() - eventLog.getStartExecute().getTime();
                eventLog.setCostTime(costTime);
            }
            //保存日志信息
            saveResult = this.saveEventLog(eventLog);
        } catch (Throwable throwable) {
            saveResult.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventLogManagerImpl[saveEventLog] is error ：" + throwable.getMessage());
        }
        return saveResult;
    }


    @Override
    public Result<List<EventLog>> listEventLog(EventLogQuery query) {
        Result<List<EventLog>> result = new Result<>();
//        HintManager hintManager = null;
        try {
//            hintManager = initHint(LOG_TABLE, query.getBizId(), query.getBizId());
//            List<EventLog> list = eventLogDao.listEventLog(query);
//            result.setModel(list);
//            result.setSuccess(true);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventLogManagerImpl[listEventLog] is error ：" + throwable.getMessage());
        } finally {
//            hintManager.close();
        }
        return result;
    }


    @Override
    public Result<Integer> countEventLog(EventLogQuery query) {
        Result<Integer> result = new Result<>();
//        HintManager hintManager = null;
        try {
//            hintManager = initHint(LOG_TABLE, query.getBizId(), query.getBizId());
//            int dataSize = eventLogDao.countEventLog(query);
//            result.setModel(dataSize);
//            result.setSuccess(true);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventLogManagerImpl[countEventLog] is error ：" + throwable.getMessage());
        } finally {
//            hintManager.close();
        }
        return result;
    }
}
