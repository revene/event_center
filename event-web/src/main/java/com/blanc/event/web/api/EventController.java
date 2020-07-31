package com.blanc.event.web.api;

import com.blanc.event.manager.EventManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventQuery;
import com.blanc.event.model.PageResult;
import com.blanc.event.model.Result;
import com.blanc.event.sevice.EventPublishService;
import com.blanc.event.web.request.EventQueryRequest;
import com.blanc.event.web.request.EventRequest;
import com.blanc.event.web.vo.EventVO;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件管理controller
 *
 * @author wangbaoliang
 */
@Api("事件管理")
@Slf4j(topic = "eventLog")
@RestController
@RequestMapping(value = "event")
public class EventController {

    @Resource
    private EventManager eventManager;
    @Resource
    private EventPublishService eventPublishService;

    /**
     * 添加一个事件
     *
     * @param eventVO 要添加的事件
     * @return 事件的id
     */
    @ApiOperation("添加事件")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<Long> add(@RequestBody EventRequest eventVO) {
        Result<Long> result = new Result<>();
        try {
            Event event = new Event();
            BeanUtils.copyProperties(eventVO, event);
            result = eventPublishService.publishEvent(event);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventController[add] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    eventVO);
        }
        return result;
    }

    /**
     * 获取事件列表
     *
     * @param query 查询参数
     * @return 事件列表
     */
    @ApiOperation("获取事件列表")
    @RequestMapping(value = "listEvent", method = RequestMethod.POST)
    public PageResult<List<EventVO>> listEvent(@RequestBody EventQueryRequest query) {
        PageResult<List<EventVO>> result = new PageResult<>();
        try {
            List<EventVO> resultDataList = new ArrayList<>();
            EventQuery eventQuery = DozerBeanMapperBuilder.buildDefault().map(query, EventQuery.class);

            Result<Integer> countResult = eventManager.countEvent(eventQuery);
            if (countResult.isSuccess()) {
                result.setTotalCount(countResult.getData());
                Result<List<Event>> queryResult = eventManager.listEvent(eventQuery);
                if (queryResult.isSuccess()) {
                    List<Event> dataList = queryResult.getData();
                    for (Event data : dataList) {
                        EventVO eventVO = DozerBeanMapperBuilder.buildDefault().map(data, EventVO.class);
                        resultDataList.add(eventVO);
                    }
                    result.success(resultDataList);
                } else {
                    result.fail(queryResult.getErrorMessage());
                }
            } else {
                result.fail(countResult.getErrorMessage());
            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventController[listEvent] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    query);
        }
        return result;
    }

    /**
     * 获取事件信息
     *
     * @param query 查询请求参数
     * @return 事件信息
     */
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
            log.error("EventController[getEvent] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    query);
        }
        return result;
    }
}
