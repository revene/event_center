package com.blanc.event.dao;

import com.blanc.event.model.EventScanNode;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 扫描节点Dao层
 *
 * @author wangbaoliang
 */
@Repository
public interface EventScanNodeDao {

    /**
     * 获取所有的扫描节点
     *
     * @return 所有的扫描节点实体
     */
    List<EventScanNode> listAll();


}
