package com.blanc.event.service.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.blanc.event.cache.EventExecuteResultCache;
import com.blanc.event.manager.EventLogManager;
import com.blanc.event.manager.EventManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventExecuteResult;
import com.blanc.event.model.EventResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadFactory;

/**
 * @author chuchengyi
 */
@Component
public class EventResultHandler implements InitializingBean, DisposableBean {


    /**
     * 功能：定义数据缓冲区的大小
     */
    private static final int BUFFER_SIZE = 1024 * 1024;


    /**
     * 功能：定义执行的最大线程数
     */
    @Value("${event.update.result.thread.size}")
    public int maxThreadSize;


    @Resource
    private EventManager eventManager;

    @Resource
    private EventLogManager eventLogManager;

    @Resource
    private EventExecuteResultCache eventExecuteResultCache;


    /**
     * 功能：高效分发的环形队列
     */
    private Disruptor<EventExecuteResult> disruptor;


    /**
     * 功能：数据对象产生的工厂
     */
    private EventExecuteFactory eventExecuteFactory = new EventExecuteFactory();


    /**
     * 功能:线程池
     */
    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("eventResult process Thread -%d").build();

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    public EventResultHandler() {
    }

    /**
     * 功能：发布事件信息
     *
     * @param event
     * @param result
     */
    public void publishEventResult(Event event, EventResult result) {
        RingBuffer<EventExecuteResult> ringBuffer = disruptor.getRingBuffer();
        //请求下一个事件序号；
        long sequence = ringBuffer.next();
        try {
            //获取该序号对应的事件对象；
            EventExecuteResult executeResult = ringBuffer.get(sequence);
            executeResult.setEvent(event);
            executeResult.setResult(result);
        } catch (Throwable throwable) {
            logger.error("EventResultHandler[publishEventResult] is error:", throwable);
        } finally {
            //发布事件；
            ringBuffer.publish(sequence);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        //当线程数小于预期的目标的时候抛出遗产
        if (maxThreadSize < 1) {
            throw new RuntimeException("EventResultHandler maxThreadSize is 0");
        }
        //构造结果处理的线程数组
        WorkHandler[] workHandlers = new WorkHandler[maxThreadSize];
        for (int i = 0; i < workHandlers.length; i++) {
            //构造事件处理器的对象
            EventResultUpdater updater = new EventResultUpdater(this.eventManager, this.eventLogManager, eventExecuteResultCache, true);
            workHandlers[i] = updater;
        }
        disruptor = new Disruptor<EventExecuteResult>(eventExecuteFactory, BUFFER_SIZE, threadFactory);
        disruptor.handleEventsWithWorkerPool(workHandlers);
        disruptor.start();
    }

    @Override
    public void destroy() throws Exception {
        try {
            disruptor.shutdown();
        } catch (Throwable throwable) {
            logger.error("EventResultHandler[destroy] is error:", throwable);
        }
    }
}
