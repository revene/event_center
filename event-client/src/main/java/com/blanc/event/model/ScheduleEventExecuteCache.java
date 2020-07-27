package com.blanc.event.model;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 功能：事件执行情况的缓存
 *
 * @author chuchengyi
 */
public class ScheduleEventExecuteCache {


    /**
     * 功能：定义实例
     */
    private static final ScheduleEventExecuteCache INSTANCE = new ScheduleEventExecuteCache();


    /**
     * 功能：阻塞队列用来做事件通知
     * todo 无界的阻塞队列，是否会导致oom
     */
    private BlockingQueue<Event> sharedQueue = new LinkedBlockingQueue();

    /**
     * 功能：服务列表信息
     */
    private Map<Event, EventResult> executeMap = new ConcurrentHashMap<>();


    private ScheduleEventExecuteCache() {

    }

    public static ScheduleEventExecuteCache getInstance() {
        return INSTANCE;
    }


    /**
     * 功能：添加一个事件
     *
     * @param event
     * @param result
     */
    public void addEventExecute(Event event, EventResult result) {
        executeMap.put(event, result);
        sharedQueue.add(event);
    }


    /**
     * 功能：删除一个事件
     *
     * @param event
     */
    public void removeEventExecute(Event event) {
        executeMap.remove(event);
    }

    /**
     * 功能：获取具体某个事件的执行结果
     *
     * @param event
     * @return
     */
    public EventResult getResult(Event event) {
        return executeMap.get(event);
    }

    public BlockingQueue<Event> getEventQueue() {
        return sharedQueue;
    }
}
