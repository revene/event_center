package com.blanc.event.web.api;

import com.blanc.event.model.Result;
import com.blanc.event.web.request.EventTypeRequest;
import com.blanc.event.web.request.EventTypeUpdateRequest;
import com.google.common.base.Throwables;
import com.blanc.event.manager.EventTypeManager;
import com.blanc.event.model.EventType;
import com.blanc.event.web.vo.EventTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事件类型管理
 *
 * @author wangbaoliang
 */
@Api(tags = "事件类型管理")
@Slf4j(topic = "eventLog")
@Controller
@RequestMapping(value = "eventType")
public class EventTypeController {

    @Resource
    private EventTypeManager eventTypeManager;

    /**
     * 添加一个时间类型
     *
     * @param eventTypeVO 要添加的事件
     * @return true or false
     */
    @ApiOperation("添加事件类型")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody EventTypeRequest eventTypeVO) {
        Result result = new Result<>();
        try {
            EventType eventType = new EventType();
            BeanUtils.copyProperties(eventTypeVO, eventType);
            eventType.setStatus(0);
            eventType.setGmtCreate(new Date());
            eventType.setGmtModify(new Date());
            result = eventTypeManager.saveEventType(eventType);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventTypeController[add] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    eventTypeVO);
        }
        return result;
    }

    /**
     * 激活一个事件
     *
     * @param eventTypeVO 要激活的事件
     * @return true or false
     */
    @ApiOperation("激活事件类型")
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result active(@RequestBody EventTypeRequest eventTypeVO) {
        Result result = new Result<>();
        try {
            Long eventTypeId = eventTypeVO.getId();
            result = eventTypeManager.activeEventType(eventTypeId);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventTypeController[active] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    eventTypeVO);
        }
        return result;
    }

    /**
     * 更新事件类型
     *
     * @param eventTypeVO 要更新的事件
     * @return true or false
     */
    @ApiOperation("添加事件类型")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody EventTypeUpdateRequest eventTypeVO) {
        Result result = new Result<>();
        try {
            EventType updateEventType = DozerBeanMapperBuilder.buildDefault().map(eventTypeVO, EventType.class);
            result = eventTypeManager.updateEventType(updateEventType);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventTypeController[update] is error, causedy by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    eventTypeVO);
        }
        return result;
    }

    /**
     * 查询所有事件类型
     *
     * @param request 查询请求参数
     * @return 事件类型列表
     */
    @ApiOperation("查询事件类型")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result<List<EventTypeVO>> list(@RequestBody EventTypeRequest request) {
        Result<List<EventTypeVO>> result = new Result<>();
        try {
            List<EventTypeVO> resultDataList = new ArrayList<>();
            Result<List<EventType>> queryResult = eventTypeManager.listEventType(request.getAppCode());
            if (queryResult.isSuccess()) {
                List<EventType> dataList = queryResult.getData();
                for (EventType data : dataList) {
                    EventTypeVO eventTypeVO = DozerBeanMapperBuilder.buildDefault().map(data, EventTypeVO.class);
                    resultDataList.add(eventTypeVO);
                }
                result.success(resultDataList);
            } else {
                result.fail(queryResult.getErrorMessage());
            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventTypeController[list] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    request);
        }
        return result;
    }
}
