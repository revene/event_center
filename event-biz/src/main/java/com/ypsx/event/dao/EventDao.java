package com.ypsx.event.dao;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventQuery;
import com.ypsx.event.model.EventType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chuchengyi
 */
@Repository
public interface EventDao {

    /**
     * 功能：插入事件信息
     *
     * @param event
     * @return
     */
    int insertEvent(Event event);


    /**
     * 功能：获取具体的事件信息
     *
     * @param appCode   应用代码
     * @param eventType 事件类型
     * @param bizId     业务标识
     * @param version   版本号
     * @return
     */
    Event getEvent(@Param("appCode") String appCode, @Param("eventType") String eventType, @Param("bizId") String bizId, @Param("version") Long version);


    /**
     * 功能：扫描时间
     *
     * @param status    事件的状态
     * @param startTime 事件的启示时间
     * @param endTime   事件的结束时间
     * @param offset    分页的偏移量
     * @param limit     分页的单页数量
     * @return event列表
     */
    List<Event> scanEvent(@Param("status") int status, @Param("startTime") long startTime, @Param("endTime") long endTime,
                          @Param("offset") long offset, @Param("limit") int limit);

    /**
     * 功能：更新事件信息
     *
     * @param event
     * @return
     */
    int updateEvent(Event event);

    /**
     * 功能：根据条件查询信息
     *
     * @param query
     * @return
     */
    List<Event> listEvent(EventQuery query);


    /**
     * 功能：统计总条数
     *
     * @param query
     * @return
     */
    int countEvent(EventQuery query);
}
