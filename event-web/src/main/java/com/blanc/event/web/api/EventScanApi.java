package com.blanc.event.web.api;

import com.blanc.event.model.Result;
import com.google.common.base.Throwables;
import com.blanc.event.entity.EventQuery;
import com.blanc.event.manager.EventScanManager;
import com.blanc.event.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chuchengyi
 */
@Controller
@RequestMapping(value = "eventScan")
public class EventScanApi {


    @Resource
    private EventScanManager eventScanManager;

    /**
     * 功能：定义日志打印信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<Event>> scan(int tableIndex) throws Exception {
        Result<List<Event>> result = null;
        try {
            long startTime = 0;
            long endTime = System.currentTimeMillis();
            EventQuery eventQuery = new EventQuery();
//            eventQuery.setStatus();
//            result = eventScanManager.scanList(tableIndex, 0, startTime, endTime, 0, 50);
        } catch (Throwable throwable) {
            result = new Result<>();
            result.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventScanApi[scan] is error" + throwable.getMessage());
        }
        return result;

    }

}
