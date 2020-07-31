package com.blanc.event.cache;

import com.blanc.event.model.EventType;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

/**
 * @author ：blanc
 * @date ：Created in 2020/7/31 下午7:11
 */
public class EventTypeCacheTest {

    /**
     * 线程池
     */
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * eventType的缓存
     */
    private EventTypeCache eventTypeCache = EventTypeCache.getInstance();

    private static final Semaphore SEMAPHORE = new Semaphore(10);

    /**
     * 测试addEventType的并发
     */
    @Test
    public void addEventType() {
        try {
            for (int i = 0 ; i < 1000; i++){
                SEMAPHORE.acquire();
                EventType eventType = new EventType();
                eventType.setAppCode("test");
                eventType.setName("testEventType");
                eventTypeCache.addEventType(eventType);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}