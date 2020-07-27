package com.blanc.event.manager;

import com.blanc.event.model.*;
import com.ypsx.event.model.*;

import java.util.List;

/**
 * @author chuchengyi
 */
public interface EventLogManager {

    /**
     * 功能：保存事件执行的日志信息
     *
     * @param eventLog
     * @return
     */
    public Result<Boolean> saveEventLog(EventLog eventLog);


    /**
     * 功能：保存日志信息
     *
     * @param result
     * @param event
     * @return
     */
    public Result<Boolean> saveEventLog(EventResult result, Event event);


    /**
     * 功能：根据查询条件查询事件信息
     *
     * @param query
     * @return
     */
    public Result<List<EventLog>> listEventLog(EventLogQuery query);


    /**
     * 功能：统计数据的条数
     *
     * @param query
     * @return
     */
    public Result<Integer> countEventLog(EventLogQuery query);
}
