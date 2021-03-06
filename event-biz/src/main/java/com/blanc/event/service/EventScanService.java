package com.blanc.event.service;

import com.blanc.event.model.Result;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventScanNode;

/**
 * 功能：事件扫描服务
 *
 * @author blanc
 */
public interface EventScanService {


    /**
     * 功能：开始扫描节点
     *
     * @param node 事件扫描节点
     * @return success or fail
     */
    Result scanEvent(EventScanNode node);


    /**
     * 功能: 取消扫描节点
     *
     * @param node 扫描节点
     * @return success or fail
     */
    Result cancelScanEvent(EventScanNode node);


    /**
     * 功能：停止服务
     *
     * @return
     */
    Result stop();

    /**
     * 功能：将事件提交到时间轮
     *
     * @param event 要提交的事件
     * @return success or fail
     */
    Result submitEvent(Event event);
}
