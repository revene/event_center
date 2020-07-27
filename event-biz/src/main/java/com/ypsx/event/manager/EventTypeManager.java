package com.ypsx.event.manager;

import com.ypsx.event.model.EventType;
import com.ypsx.util.model.Result;

import java.util.List;

/**
 * 功能：事件类型的业务实现
 *
 * @author chuchengyi
 */
public interface EventTypeManager {

    /**
     * 功能：保存事件类型信息
     *
     * @param eventType
     * @return
     */
    public Result<Boolean> saveEventType(EventType eventType);


    /**
     * 功能：获取所有的事件类型的列表
     *
     * @return
     */
    public Result<List<EventType>> listAll();


    /**
     * 功能：功能通过运用标识来获取事件类型的列表
     *
     * @param appCode
     * @return
     */
    Result<List<EventType>> listEventType(String appCode);


    /**
     * 功能：更新事件类型
     *
     * @param updateEventType
     * @return
     */
    Result<Boolean> updateEventType(EventType updateEventType);


    /**
     * 功能：激活事件信息
     *
     * @param eventTypeId
     * @return
     */
    Result<Boolean> activeEventType(Long eventTypeId);
}
