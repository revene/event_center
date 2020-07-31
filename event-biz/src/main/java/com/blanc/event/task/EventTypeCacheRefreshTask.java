package com.blanc.event.task;

import com.blanc.event.cache.EventTypeCache;
import com.blanc.event.manager.EventTypeManager;
import com.blanc.event.model.EventType;
import com.blanc.event.model.Result;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 事件类型event_type同步本地缓存定时任务
 */
@Component
public class EventTypeCacheRefreshTask implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * cron表达式,6分钟刷新一次
     */
    private static final String SCHEDULED_CRON = "0 0/6 * * * ?";

    @Resource
    private EventTypeManager eventTypeManager;

    /**
     * 定时任务加载本地缓存
     */
    @Scheduled(cron = SCHEDULED_CRON)
    public void cacheUpdate() {
        //获取所有的事件类型信息
        Result<List<EventType>> result = eventTypeManager.listAll();
        if (result.isSuccess()) {
            //获取数据列表信息
            List<EventType> dataList = result.getData();
            for (EventType eventType : dataList) {
                //讲数据添加到缓存中
                EventTypeCache.getInstance().addEventType(eventType);
            }
        }
    }

    /**
     * 容器初始化完毕后开始执行
     *
     * @param event applicationContext初始化事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //web容器加载的时候才执行
        if (event.getApplicationContext().getParent() == null) {
            cacheUpdate();
        }
    }
}
