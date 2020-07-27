package com.ypsx.event.manager.impl;

import com.google.common.base.Throwables;
import com.ypsx.event.dao.EventScanNodeDao;
import com.ypsx.event.manager.EventScanNodeManager;
import com.ypsx.event.model.EventScanNode;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
