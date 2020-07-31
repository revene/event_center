package com.blanc.event.task;

import com.blanc.event.service.EventExecuteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能:初始化服务初始化信息
 *
 * @author chuchengyi
 */
@Component
public class ConsumerServiceInitTask implements ApplicationListener<ContextRefreshedEvent> {


    @Resource
    private EventExecuteService eventExecuteService;

    /**
     * 功能：30s刷新一次
     */
    private static final String SCHEDULED_CRON = "0 0/1 * * * ?";

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("taskLog");


    /**
     * 功能：扫描数据分发任务
     */
    @Scheduled(cron = SCHEDULED_CRON)
    public void initConsumerService() {
        logger.info("ConsumerServiceInitTask[initConsumerService] is starting");
        eventExecuteService.initService();
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            initConsumerService();
        }
    }
}
