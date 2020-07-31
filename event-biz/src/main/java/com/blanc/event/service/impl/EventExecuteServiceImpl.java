package com.blanc.event.service.impl;

import com.blanc.event.cache.EventExecuteCache;
import com.blanc.event.cache.EventExecuteResultCache;
import com.blanc.event.cache.EventTypeCache;
import com.blanc.event.cache.ExecuteServiceCache;
import com.blanc.event.manager.EventTypeManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventType;
import com.blanc.event.model.Result;
import com.blanc.event.service.EventExecuteService;
import com.blanc.event.service.register.ConsumerServiceFactory;
import com.blanc.event.sevice.ConsumerService;
import com.blanc.event.worker.EventExecuteTask;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

/**
 * 时间执行服务
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "eventLog")
@Component
public class EventExecuteServiceImpl implements EventExecuteService, InitializingBean {

    @Resource
    private EventTypeManager eventTypeManager;
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


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //设置一个无界队列
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue(Integer.MAX_VALUE);
            //设置线程工厂
            ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("event execute Thread -%d").build();
            //设置线程池
            this.threadPool = new ThreadPoolExecutor(minThreadSize, maxThreadSize, KEEP_LIVE_TIME,
                    TimeUnit.MILLISECONDS, queue, factory);
        } catch (Throwable throwable) {
            log.error("EventExecuteServiceImpl[init] is error:" + throwable.getMessage());
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
            log.error("EventExecuteServiceImpl[executeEvent]  is error event:" + event.toString(), throwable);

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
            log.error("EventExecuteServiceImpl[submitEvent]  is error, caused by {}, event is {}",
                    Throwables.getStackTraceAsString(throwable),
                    event);
        }
        return executeResult;
    }

}
