package com.ypsx.event.manager;

import com.ypsx.event.entity.EventQuery;
import com.ypsx.event.model.Event;
import com.ypsx.util.model.Result;

import java.util.List;

/**
 * 事件扫描manager
 *
 * @author wangbaoliang
 */
public interface EventScanManager {

    /**
     * 功能：扫描数据信息
     *
     * @param tableIndex 表索引,用于hint分片算法
     * @param eventQuery 事件查询对象
     * @return 事件列表
     */
    Result<List<Event>> scanList(EventQuery eventQuery, int tableIndex);

}
