package com.blanc.event.manager.impl;

import com.blanc.event.manager.EventScanNodeManager;
import com.blanc.event.model.EventScanNode;
import com.blanc.event.dao.EventScanNodeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 事件扫描节点event
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "eventLog")
@Component
public class EventScanNodeManagerImpl implements EventScanNodeManager {

    @Resource
    private EventScanNodeDao eventScanNodeDao;


    @Override
    public List<EventScanNode> listAll() {
        return eventScanNodeDao.listAll();
    }
}
