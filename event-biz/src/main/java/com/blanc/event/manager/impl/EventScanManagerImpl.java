package com.blanc.event.manager.impl;

import com.blanc.event.model.Result;
import com.google.common.base.Throwables;
import com.blanc.event.dao.EventDao;
import com.blanc.event.entity.EventQuery;
import com.blanc.event.manager.EventScanManager;
import com.blanc.event.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 事件扫描实现类
 *
 * @author blanc
 */
@Slf4j
@Component
public class EventScanManagerImpl extends AbstractHintManager implements EventScanManager {


    /**
     * 功能：表名
     */
    private static final String TABLE = "event";

    @Resource
    private EventDao eventDao;

    /**
     * 扫描event表
     *
     * @param tableIndex 用于hint表达式的表索引
     * @param eventQuery 事件的查询对象
     * @return 事件列表
     */
    @Override
    public Result<List<Event>> scanList(EventQuery eventQuery, int tableIndex) {
        Result<List<Event>> result = new Result<>();
        //try-with-resource语法初始化hintManager,结束自动关闭
        try (HintManager hintManager = initHint(EVENT_TABLE, tableIndex);) {
            List<Event> dataList = eventDao.scanEvent(eventQuery.getStatus(), eventQuery.getStartTime(),
                    eventQuery.getEndTime(), eventQuery.getOffset(), eventQuery.getLimit());
            result.success(dataList);
        } catch (Throwable throwable) {
            result.fail(Throwables.getStackTraceAsString(throwable));
            log.error("EventScanManagerImpl[scanList] is error, caused by {}", Throwables.getStackTraceAsString(throwable));
        }
        return result;
    }


}
