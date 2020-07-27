package com.blanc.event.manager.impl;

import com.blanc.event.model.*;
import com.google.common.base.Throwables;
import com.blanc.event.dao.EventTypeDao;
import com.blanc.event.error.ExceptionConstant;
import com.blanc.event.error.ExceptionUtil;
import com.blanc.event.manager.EventManager;
import com.blanc.event.manager.EventTypeManager;
import com.ypsx.event.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author chuchengyi
 */
@Component
public class EventTypeManagerImpl implements EventTypeManager {

    @Resource
    private EventTypeDao eventTypeDao;

    @Resource
    private EventManager eventManager;

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


//    /**
//     * 功能：检测参数信息是否正确
//     *
//     * @param eventType
//     * @return
//     */
//    private ExceptionInfo checkParam(EventType eventType) {
//        //判断应用标识是否为空
//        if (isEmpty(eventType.getAppCode())) {
//            return ExceptionConstant.PARAM_IS_NULL;
//        }
//        //判断事件类型是否为空
//        if (isEmpty(eventType.getEventType())) {
//            return ExceptionConstant.PARAM_IS_NULL;
//        }
//        //判断最大执行次数
//        if (eventType.getMaxExecuteSize() == 0) {
//            eventType.setMaxExecuteSize(Integer.MAX_VALUE);
//        }
//        //判断是否是定时任务是否为空
//        if (isNULL(eventType.getSchedule())) {
//            return ExceptionConstant.PARAM_IS_NULL;
//        }
//        //判断时间间隔是否为空
//        if (eventType.getSchedule() == 0) {
//            if (isNULL(eventType.getScheduleTime())) {
//                return ExceptionConstant.PARAM_IS_NULL;
//            }
//        }
//        //判断定时任务的表达式是否为空
//        else {
//            if (isEmpty(eventType.getCronExpression())) {
//                return ExceptionConstant.PARAM_IS_NULL;
//            }
//        }
//        return ExceptionConstant.OK;
//    }

    @Override
    public Result<Boolean> saveEventType(EventType eventType) {
        Result<Boolean> result = new Result<>();
        try {
            //检测输入信息是是否正确
//            ExceptionInfo exceptionCode = checkParam(eventType);
//            if (exceptionCode != ExceptionConstant.OK) {
//                ExceptionUtil.setException(result, exceptionCode);
//                return result;
//            }
            String appCode = eventType.getAppCode();
            String type = eventType.getEventType();
            //判断下是否已经存在
            EventType exist = eventTypeDao.getEventType(appCode, type, null);
            if (exist != null) {
                ExceptionUtil.setException(result, ExceptionConstant.EVENT_TYPE_IS_EXIST);
            } else {
                eventTypeDao.insert(eventType);
                result.success();
            }
        } catch (Exception e) {
            result.fail(Throwables.getStackTraceAsString(e));
            logger.error("EventTypeManagerImpl[saveEventType] is error :" + e.getMessage());
        }
        return result;
    }


    @Override
    public Result<List<EventType>> listEventType(String appCode) {
        Result<List<EventType>> result = new Result<>();
        try {
            //获取所有的事件类型
            List<EventType> dataList = eventTypeDao.listEventType(appCode);
            result.success(dataList);
        } catch (Exception e) {
            result.fail(Throwables.getStackTraceAsString(e));
            logger.error("EventTypeManagerImpl[listEventType] is error :" + e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<EventType>> listAll() {
        Result<List<EventType>> result = new Result<>();
        try {
            //获取所有的事件类型
            List<EventType> dataList = eventTypeDao.listAll();
            result.success(dataList);
        } catch (Exception e) {
            result.fail(Throwables.getStackTraceAsString(e));
            logger.error("EventTypeManagerImpl[listAll] is error :" + e.getMessage());
        }
        return result;
    }


    @Override
    public Result updateEventType(EventType updateEventType) {
        Result result = new Result();
        try {
            eventTypeDao.updateEventType(updateEventType);
            result.success();
        } catch (Exception e) {
            result.fail(Throwables.getStackTraceAsString(e));
            logger.error("EventTypeManagerImpl[saveEventType] is error :" + e.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> activeEventType(Long eventTypeId) {
        Result<Boolean> result = new Result<>();
        try {
            EventType updateEventType = eventTypeDao.getEventType(null, null, eventTypeId);
            if (updateEventType != null) {
                updateEventType.setStatus(EventTypeStatus.ACTIVE.getStatus());
                int schedule = updateEventType.getSchedule().intValue();
                if (EventTaskType.isScheduleTask(schedule)) {
                    Event event = createEvent(updateEventType);
                    Result<String> eventResult = eventManager.saveEvent(event);
                    if (!eventResult.isSuccess()) {
                        throw new Exception(eventResult.getErrorMessage());
                    }
                }
                eventTypeDao.updateEventType(updateEventType);
            }
            result.success();
        } catch (Exception e) {
            result.fail(Throwables.getStackTraceAsString(e));
            logger.error("EventTypeManagerImpl[activeEventType] is error :" + e.getMessage());
        }
        return result;
    }


    /**
     * 功能：构造事件信息
     *
     * @param eventType
     * @return
     */
    private Event createEvent(EventType eventType) {
        Event event = new Event();
        event.setAppCode(eventType.getAppCode());
        event.setEventType(eventType.getEventType());
        event.setBizId(eventType.getEventType());
        event.setVersion(0L);
        event.setExecuteSize(0L);
        //构造cron表达式解析类
        CronSequenceGenerator generator = new CronSequenceGenerator(eventType.getCronExpression());
        Date next = generator.next(new Date());
        event.setExecuteTime(next.getTime());
        event.setGmtCreate(new Date());
        event.setGmtModify(new Date());
        return event;
    }

}
