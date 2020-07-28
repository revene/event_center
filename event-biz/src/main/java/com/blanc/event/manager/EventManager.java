package com.blanc.event.manager;

import com.blanc.event.model.Result;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventQuery;

import java.util.List;

/**
 * 事件管理manager
 *
 * @author wangbaoliang
 */
public interface EventManager {


    /**
     * 功能：保存事件
     *
     * @param event
     * @return
     */
    Result<Long> saveEvent(Event event);


    /**
     * 功能：更新事件信息
     *
     * @param event
     * @return
     */
    Result updateEvent(Event event);


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
    Result<Integer> countEvent(EventQuery query);
}
