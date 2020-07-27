package com.ypsx.event.task;

import com.ypsx.event.cache.EventTypeCache;
import com.ypsx.event.manager.EventTypeManager;
import com.ypsx.event.model.EventType;
import com.ypsx.util.model.Result;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：事件类型本地缓存同步任务
 *
 * @author chuchengyi
 */
@Component
public class EventTypeCacheRefreshTask implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * 功能：定义事件类型缓存的刷新时长
     */

    private static final String SCHEDULED_CRON = "0 0/6 * * * ?";


    @Resource
    private EventTypeManager eventTypeManager;

    /**
     * 功能：定时刷新事件类型信息
     *
     * @return
     */
    @Scheduled(cron = SCHEDULED_CRON)
    public Result<List<EventType>> cacheUpdate() {
        //获取事件类型信息
        Result<List<EventType>> result = eventTypeManager.listAll();
        if (result.isSuccess()) {
            //获取数据列表信息
            List<EventType> dataList = result.getModel();
            for (EventType eventType : dataList) {
                //讲数据添加到缓存中
                EventTypeCache.getInstance().addEventType(eventType);
            }
        }
        return result;
    }

    /**
     * 功能：容器启动的时候自动执行
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //web容器加载的时候才执行
        if (event.getApplicationContext().getParent() == null) {
            //执行缓存
            Result<List<EventType>> result = cacheUpdate();
            if (!result.isSuccess()) {
                throw new RuntimeException("EventTypeCacheRefreshTask is error" + result.getErrorMessage());
            }
        }
    }
}
