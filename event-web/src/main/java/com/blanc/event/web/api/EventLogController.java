package com.blanc.event.web.api;

import com.blanc.event.manager.EventLogManager;
import com.blanc.event.model.EventLog;
import com.blanc.event.model.EventLogQuery;
import com.blanc.event.model.PageResult;
import com.blanc.event.model.Result;
import com.blanc.event.web.request.EventLogQueryRequest;
import com.blanc.event.web.vo.EventLogVO;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事件日志管理controller
 *
 * @author wangbaoliang
 */
@Api(tags = "事件日志管理")
@Slf4j(topic = "eventLog")
@RestController
@RequestMapping(value = "eventLog")
public class EventLogController {

    @Resource
    private EventLogManager eventLogManager;

    /**
     * 增加一条事件的日志
     *
     * @param bizId 业务id
     * @return true or false
     */
    @ApiOperation("增加一条事件的日志")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<Boolean> add(@RequestBody String bizId) {
        Result<Boolean> result = new Result<>();
        try {
            EventLog eventLog = new EventLog();
            eventLog.setAppCode("test");
            eventLog.setEventType("2222");
            eventLog.setBizId(bizId);
            eventLog.setErrorMessage("1111");
            eventLog.setExecuteIp("127.0.0.1");
            eventLog.setSuccess(0);
            eventLog.setExecuteIndex(0);
            eventLog.setExecuteTime(new Date());
            result = eventLogManager.saveEventLog(eventLog);
            result.success();
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventLogController[add] is error, caused by {}, param is{}",
                    Throwables.getStackTraceAsString(throwable),
                    bizId);
        }
        return result;

    }

    /**
     * 查询事件执行日志
     *
     * @param queryRequest 查询参数
     * @return 分页相应
     * @throws Exception
     */
    @ApiOperation("查询事件执行日志")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public PageResult<List<EventLogVO>> list(@RequestBody EventLogQueryRequest queryRequest) {
        PageResult<List<EventLogVO>> result = new PageResult<>();
        try {
            //构造线程对象
            EventLogQuery query = DozerBeanMapperBuilder.buildDefault().map(queryRequest, EventLogQuery.class);
            //执行查询
            Result<Integer> countResult = eventLogManager.countEventLog(query);
            //查询成功
            if (!countResult.isSuccess()) {
                result.fail(countResult.getErrorMessage());
                return result;
            }
            result.setTotalCount(countResult.getData());
            List<EventLogVO> list = new ArrayList<>();
            Result<List<EventLog>> queryResult = eventLogManager.listEventLog(query);
            if (queryResult.isSuccess()) {
                List<EventLog> dataList = queryResult.getData();
                for (EventLog eventLog : dataList) {
                    EventLogVO eventTypeVO = DozerBeanMapperBuilder.buildDefault().map(eventLog, EventLogVO.class);
                    list.add(eventTypeVO);
                }
                result.success(list);
            } else {
                result.fail(queryResult.getErrorMessage());
            }
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventLogController[list] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    queryRequest);
        }
        return result;
    }


}
