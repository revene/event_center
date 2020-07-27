package com.ypsx.event.web.api;

import com.google.common.base.Throwables;
import com.ypsx.event.manager.EventTypeManager;
import com.ypsx.event.model.EventType;
import com.ypsx.event.model.Result;
import com.ypsx.event.web.request.EventTypeRequest;
import com.ypsx.event.web.request.EventTypeUpdateRequest;
import com.ypsx.event.web.vo.EventTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 事件类型web端
 *
 * @author wangbaoliang
 */
@Controller
@RequestMapping(value = "eventType")
@Api(description = "事件类型管理接口", tags = "事件类型管理")
public class EventTypeApi {


    @Resource
    private EventTypeManager eventTypeManager;

    /**
     * 功能：定义日志打印信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @ResponseBody
    @ApiOperation("添加事件类型")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<Boolean> add(@RequestBody EventTypeRequest eventTypeVO) throws Exception {
        Result<Boolean> result = new Result<>();
        try {
            EventType eventType = new EventType();
            BeanUtils.copyProperties(eventTypeVO, eventType);
            eventType.setStatus(0);
            eventType.setGmtCreate(new Date());
            eventType.setGmtModify(new Date());
            result = eventTypeManager.saveEventType(eventType);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventTypeApi[add] is error" + throwable.getMessage());

        }
        return result;
    }


    @ResponseBody
    @ApiOperation("激活事件类型")
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result<Boolean> active(@RequestBody EventTypeRequest eventTypeVO) throws Exception {
        Result<Boolean> result = new Result<>();
        try {
            Long eventTypeId = eventTypeVO.getId();
            result = eventTypeManager.activeEventType(eventTypeId);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventTypeApi[active] is error" + throwable.getMessage());
        }
        return result;
    }


    @ResponseBody
    @ApiOperation("添加事件类型")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result<Boolean> update(@RequestBody EventTypeUpdateRequest eventTypeVO) throws Exception {
        Result<Boolean> result = new Result<>();
        try {
            EventType updateEventType = DozerBeanMapperBuilder.buildDefault().map(eventTypeVO, EventType.class);
            result = eventTypeManager.updateEventType(updateEventType);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventTypeApi[update] is error" + throwable.getMessage());
        }
        return result;
    }


    @ResponseBody
    @ApiOperation("查询事件类型")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result<List<EventTypeVO>> list(@RequestBody EventTypeRequest request) throws Exception {
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
            logger.error("EventTypeApi[list] is error" + throwable.getMessage());

        }
        return result;

    }
}
