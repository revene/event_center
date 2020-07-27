package com.blanc.event.dao;

import com.blanc.event.model.EventType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能：事件类型的dao
 *
 * @author chuchengyi
 */
@Repository
public interface EventTypeDao {


    /**
     * 功能：插入事件类型
     *
     * @param eventType
     * @return
     */
    public int insert(EventType eventType);


    /**
     * 功能：获取到所有的事件类型
     *
     * @return
     */
    public List<EventType> listAll();


    /**
     * 功能：根据应用系统代码获取事件类型
     *
     * @param appCode
     * @return
     */
    public List<EventType> listEventType(@Param("appCode") String appCode);


    /**
     * 功能：通过应用代码和事件类型来查询事件类型信息
     *
     * @param appCode
     * @param eventType
     * @param id
     * @return
     */
    public EventType getEventType(@Param("appCode") String appCode, @Param("eventType") String eventType, @Param("id") Long id);


    /**
     * 功能：更新事件信息类型
     *
     * @param updateEventType
     * @return
     */
    public int updateEventType(EventType updateEventType);

}
