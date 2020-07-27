package com.ypsx.event.service.impl;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ypsx.event.cache.EventExecuteCache;
import com.ypsx.event.cache.EventExecuteResultCache;
import com.ypsx.event.cache.EventTypeCache;
import com.ypsx.event.cache.ExecuteServiceCache;
import com.ypsx.event.manager.EventManager;
import com.ypsx.event.manager.EventTypeManager;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventType;
import com.ypsx.event.model.Result;
import com.ypsx.event.service.EventExecuteService;
import com.ypsx.event.service.register.ConsumerServiceFactory;
import com.ypsx.event.sevice.ConsumerService;
import com.ypsx.event.worker.EventExecuteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author chuchengyi
 */

@Component
public class EventExecuteServiceImpl implements EventExecuteService, InitializingBean {


    @Resource
    private EventTypeManager eventTypeManager;

    @Resource
    private EventManager eventManager;

    @Resource
    private EventExecuteCache eventExecuteCache;

    @Resource
    private EventExecuteResultCache eventExecuteResultCache;

    @Resource
    private ConsumerServiceFactory consumerServiceFactory;

    /**
     * 功能：定义执行的最小线程数
     */
    @Value("${event.execute.thread.min.size}")
    public int minThreadSize;

    /**
     * 功能：定义执行的最大线程数
     */
    @Value("${event.execute.thread.max.size}")
    public int maxThreadSize;

    /**
     * 功能：线程初始化时间
     */
    private static final long KEEP_LIVE_TIME = 0;
    /**
     * 功能：线程池
     */
    private ExecutorService threadPool;

    /**
     * 功能：事件类型的缓存信息
     */
    private EventTypeCache eventTypeCache = EventTypeCache.getInstance();

    /**
     * 功能：执行服务的缓存
     */
    private ExecuteServiceCache executeServiceCache = ExecuteServiceCache.getInstance();


    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    public EventExecuteServiceImpl() {

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //设置一个派对队列
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue(Integer.MAX_VALUE);
            //设置线程工厂
            ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("event execute Thread -%d").build();
            //设置线程池
            this.threadPool = new ThreadPoolExecutor(minThreadSize, maxThreadSize, KEEP_LIVE_TIME,
                    TimeUnit.MILLISECONDS, queue, factory);
        } catch (Throwable throwable) {
            logger.error("EventExecuteServiceImpl[init] is error:" + throwable.getMessage());
        }

    }

    @Override
    public void initService() {
        Result<List<EventType>> result = eventTypeManager.listAll();
        if (result.isSuccess()) {
            consumerServiceFactory.registerService(result.getData());
        }
    }


    /**
     * 功能：根据事件类型来获取服务
     *
     * @param event
     * @return
     */
    @Override
    public ConsumerService getService(Event event) {
        return consumerServiceFactory.getService(event);
    }

    /**
     * 功能：执行事件信息
     *
     * @param event
     */
    @Override
    public Result executeEvent(Event event) {
        Result executeResult = new Result();
        try {
            //功能：获取执行的服务信息
            ConsumerService service = getService(event);
            if (service != null) {
                //提交执行任务
                EventExecuteTask task = new EventExecuteTask(event, service, eventExecuteCache, eventExecuteResultCache);
                //直接执行
                task.run();
                executeResult.success();
            } else {
                executeResult.fail("NO SERVICE PROVIDER");
            }
        } catch (Throwable throwable) {
            executeResult.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventExecuteServiceImpl[executeEvent]  is error event:" + event.toString(), throwable);

        }
        return executeResult;
    }


    @Override
    public Result submitEvent(Event event) {
        Result executeResult = new Result();
        try {
            //功能：获取服务信息
            ConsumerService service = getService(event);
            if (service != null) {
                //提交执行任务
                EventExecuteTask task = new EventExecuteTask(event, service, eventExecuteCache, eventExecuteResultCache);
                //提交任务信息
                threadPool.submit(task);
                executeResult.success();
            } else {
                executeResult.fail("NO SERVICE PROVIDER");
            }

        } catch (Throwable throwable) {
            executeResult.fail(Throwables.getStackTraceAsString(throwable));
            logger.error("EventExecuteServiceImpl[submitEvent]  is error event:" + event.toString(), throwable);
        }
        return executeResult;
    }

    public int getMinThreadSize() {
        return minThreadSize;
    }

    public void setMinThreadSize(int minThreadSize) {
        this.minThreadSize = minThreadSize;
    }

    public int getMaxThreadSize() {
        return maxThreadSize;
    }

    public void setMaxThreadSize(int maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
    }
}
