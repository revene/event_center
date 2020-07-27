package com.ypsx.event.web.api;

import com.google.common.base.Throwables;
import com.ypsx.event.manager.EventManager;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventQuery;
import com.ypsx.event.model.Result;
import com.ypsx.event.sevice.EventPublishService;
import com.ypsx.event.web.request.EventQueryRequest;
import com.ypsx.event.web.request.EventRequest;
import com.ypsx.event.web.vo.EventVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.DozerBeanMapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chuchengyi
 */
@Controller
@RequestMapping(value = "event")
@Api(description = "事件接口", tags = "事件管理")
public class EventApi {


    @Resource
    private EventManager eventManager;

    @Resource
    private EventPublishService eventPublishService;

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    @ResponseBody
    @ApiOperation("添加事件")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<String> add(@RequestBody EventRequest eventVO) throws Exception {
        Result<String> result = null;
        try {
            Event event = new Event();
            BeanUtils.copyProperties(eventVO, event);
            result = eventPublishService.publishEvent(event);
        } catch (Throwable throwable) {
            result = new Result<>();
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventApi[add] is error :" + throwable.getMessage());
        }
        return result;
    }






//    @ResponseBody
//    @ApiOperation("获取事件列表")
//    @RequestMapping(value = "listEvent", method = RequestMethod.POST)
//    public PageResult<List<EventVO>> listEvent(@RequestBody EventQueryRequest query) {
//        PageResult<List<EventVO>> result = new PageResult<List<EventVO>>();
//        try {
//            List<EventVO> resultDataList = new ArrayList<>();
//            EventQuery eventQuery = DozerBeanMapperBuilder.buildDefault().map(query, EventQuery.class);
//
//            Result<Integer> countResult = eventManager.countEvent(eventQuery);
//            if (countResult.isSuccess()) {
//                result.setTotal(countResult.getModel());
//                Result<List<Event>> queryResult = eventManager.listEvent(eventQuery);
//                if (queryResult.isSuccess()) {
//                    List<Event> dataList = queryResult.getModel();
//                    for (Event data : dataList) {
//                        EventVO eventVO = DozerBeanMapperBuilder.buildDefault().map(data, EventVO.class);
//                        resultDataList.add(eventVO);
//                    }
//                    result.setModel(resultDataList);
//                    result.setSuccess(true);
//                } else {
//                    result.setException(queryResult.getExceptionInfo());
//                }
//            } else {
//                result.setException(countResult.getExceptionInfo());
//            }
//        } catch (Throwable throwable) {
//            result.setErrorMessage(throwable.getMessage());
//            logger.error("EventApi[listEvent] is error :" + throwable.getMessage());
//        }
//        return result;
//    }


    @ResponseBody
    @ApiOperation("获取事件信息")
    @RequestMapping(value = "getEvent", method = RequestMethod.POST)
    public Result<EventVO> getEvent(@RequestBody EventQueryRequest query) {
        Result<EventVO> result = new Result<EventVO>();
        try {
            EventQuery eventQuery = DozerBeanMapperBuilder.buildDefault().map(query, EventQuery.class);
            Result<List<Event>> queryResult = eventManager.listEvent(eventQuery);
            if (queryResult.isSuccess()) {
                List<Event> dataList = queryResult.getData();
                if (!CollectionUtils.isEmpty(dataList)) {
                    EventVO eventVO = DozerBeanMapperBuilder.buildDefault().map(dataList.get(0), EventVO.class);
                    result.success(eventVO);
                }
            } else {
                result.fail(queryResult.getErrorMessage());
            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventApi[getEvent] is error :" + throwable.getMessage());
        }
        return result;
    }
}
