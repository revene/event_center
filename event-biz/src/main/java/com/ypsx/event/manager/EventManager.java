package com.ypsx.event.manager;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventQuery;
import com.ypsx.util.model.Result;

import java.util.List;

/**
 * 功能：事件管理的功能
 *
 * @author chuchengyi
 */
public interface EventManager {


    /**
     * 功能：保存事件
     *
     * @param event
     * @return
     */
    public Result<String> saveEvent(Event event);


    /**
     * 功能：更新事件信息
     *
     * @param event
     * @return
     */
    public Result<Boolean> updateEvent(Event event);


    /**
     * 功能：根据条件查询信息
     *
     * @param query
     * @return
     */
    Result<List<Event>> listEvent(EventQuery query);


    /**
     * 功能：统计记录的总条数
     *
     * @param query
     * @return
     */
    public Result<Integer> countEvent(EventQuery query);
}
