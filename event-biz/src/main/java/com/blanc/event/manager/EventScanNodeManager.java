package com.blanc.event.manager;

import com.blanc.event.model.EventScanNode;

import java.util.List;

/**
 * 事件扫描节点Manager
 *
 * @author wangbaoliang
 */
public interface EventScanNodeManager {

    /**
     * 功能：获取到所有的event_scan数据库列表
     *
     * @return 事件扫描节点
     */
    List<EventScanNode> listAll();
}
