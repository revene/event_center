package com.blanc.event.cache;

import com.blanc.event.model.Event;
import com.blanc.event.model.EventType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * eventType的缓存
 */
public class EventTypeCache {

    /**
     * 单例
     */
    private static final EventTypeCache INSTANCE = new EventTypeCache();

    /**
     * 功能：存储应用事件类型的缓存信息 一级key是appCode 二级的key是eventTypeCode
     */
    private Map<String, Map<String, EventType>> eventTypeMap = new ConcurrentHashMap<>();

    private EventTypeCache() {

    }

    /**
     * 功能：获取缓存的的实例对象
     *
     * @return
     */
    public static EventTypeCache getInstance() {
        return INSTANCE;
    }

    /**
     * 功能：更加应用标识和事件类型标识来获取事件类型信息 不存在的是否返回为空
     *
     * @param appCode       应用code
     * @param eventTypeCode event_type
     * @return eventType实体
     */
    public EventType getEventType(String appCode, String eventTypeCode) {
        //判断缓存中是否已经存在应用类型
        if (eventTypeMap.containsKey(appCode)) {
            //获取具体的事件类型信息
            Map<String, EventType> map = eventTypeMap.get(appCode);
            return map.get(eventTypeCode);
        }
        //不存在返回空
        else {
            return null;
        }
    }


    /**
     * 功能：根据事件来获取事件类型
     *
     * @param event 事件
     * @return eventType
     */
    public EventType getEventType(Event event) {
        return this.getEventType(event.getAppCode(), event.getEventType());

    }

    /**
     * 功能：添加事件类型
     *
     * @param eventType 事件类型
     */
    public void addEventType(EventType eventType) {
        //获取应用标识
        String appCode = eventType.getAppCode();
        //获取事件类型标识
        String typeCode = eventType.getEventType();
        //加锁是因为其中包含了if等竞态条件,会出现并发问题
        synchronized (this.eventTypeMap) {
            //判断事件类型是否已经存在
            if (eventTypeMap.containsKey(appCode)) {
                eventTypeMap.get(appCode).put(typeCode, eventType);
            }
            //不存在的时候构造map初始化
            else {
                Map<String, EventType> map = new ConcurrentHashMap<>(32);
                map.put(typeCode, eventType);
                this.eventTypeMap.put(appCode, map);
            }
        }
    }
}
