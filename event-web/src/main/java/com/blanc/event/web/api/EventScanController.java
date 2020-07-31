package com.blanc.event.web.api;

import com.blanc.event.model.EventStatus;
import com.blanc.event.model.Result;
import com.google.common.base.Throwables;
import com.blanc.event.entity.EventQuery;
import com.blanc.event.manager.EventScanManager;
import com.blanc.event.model.Event;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 事件扫描controller
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "eventLog")
@RestController
@RequestMapping(value = "eventScan")
public class EventScanController {

    @Resource
    private EventScanManager eventScanManager;

    /**
     * 扫描事件
     *
     * @param tableIndex hint的index
     * @return 扫描的事件
     */
    @ApiOperation("扫描事件")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result<List<Event>> scan(int tableIndex) {
        Result<List<Event>> result = new Result<>();
        try {
            long startTime = 0;
            long endTime = System.currentTimeMillis();
            EventQuery eventQuery = new EventQuery();
            eventQuery.setStatus(EventStatus.ACTIVE.getStatus());
            eventQuery.setLimit(50);
            eventQuery.setOffset(0);
            eventQuery.setStartTime(startTime);
            eventQuery.setEndTime(endTime);
            result = eventScanManager.scanList(eventQuery, tableIndex);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventScanController[scan] is error, caused by {}, param is {}",
                    Throwables.getStackTraceAsString(throwable),
                    tableIndex);
        }
        return result;
    }

}
