package com.blanc.event.service.register;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.blanc.event.cache.EventTypeCache;
import com.blanc.event.cache.ExecuteServiceCache;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventType;
import com.blanc.event.sevice.ConsumerService;
import com.blanc.event.sevice.EventConsumerService;
import com.blanc.event.sevice.EventScheduleConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chuchengyi
 */
@Component
public class ConsumerServiceFactory {


    @Autowired
    private ScheduleConsumerServiceRegister scheduleServiceRegister;


    @Autowired
    private NormalConsumerServiceRegister normalServiceRegister;


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
    private final static Logger logger = LoggerFactory.getLogger("registerLog");


    public void registerService(List<EventType> typeList) {
        try {
            //注册普通服务接口
            Set<String> appList = getNormalServiceApp(typeList);
            EventConsumerService object = null;
            for (String app : appList) {
                //当没有注册的时候才来注册
                if (!normalServiceRegister.hadRegisterService(app, EventConsumerService.class)) {
                    normalServiceRegister.registerService(app, EventConsumerService.class);
                }
            }

            //注册定时任务的服务消费接口
            Set<String> scheduleAppList = getScheduleServiceApp(typeList);
            EventScheduleConsumerService service = null;
            for (String app : scheduleAppList) {
                //当没有注册的时候才来注册
                if (!scheduleServiceRegister.hadRegisterService(app, EventScheduleConsumerService.class)) {
                    scheduleServiceRegister.registerService(app, EventScheduleConsumerService.class);
                }
            }
        } catch (Throwable throwable) {
            logger.error("ConsumerServiceRegisterFactory[registerService] is error:", throwable);
        }


    }


    /**
     * 功能：获取服务信息
     *
     * @param event
     * @return
     */
    public ConsumerService getService(Event event) {
        String app = event.getAppCode();
        String targetIp = event.getTargetIp();
        ConsumerService service = null;
        //当目标IP为空的时候直接调用
        EventType eventType = eventTypeCache.getEventType(app, event.getEventType());
        if (eventType.getSchedule() == 0) {
            service = getNormalService(app, targetIp);
        } else {
            service = getScheduleService(app, targetIp);
        }
        return service;
    }


    /**
     * 功能：获取到正常的消费服务信息
     *
     * @param app
     * @param targetIp
     * @return
     */
    private EventConsumerService getNormalService(String app, String targetIp) {
        EventConsumerService service = null;
        ReferenceConfig<EventConsumerService> reference = null;
        //当IP没有的时候调用普通服务
        if (StringUtils.isEmpty(targetIp)) {
            reference = executeServiceCache.getService(app);

        }
        //制定目标ip调用的直接调用对应的机器
        else {
            //当没有注册的时候才去注册
            if (!normalServiceRegister.hadRegisterDirectService(app, targetIp, EventConsumerService.class)) {
                normalServiceRegister.registerDirectService(app, targetIp, EventConsumerService.class);
            }
            reference = executeServiceCache.getDirectService(app, targetIp);
        }
        //判断reference 是否存在
        if (reference != null) {
            service = reference.get();
        }
        return service;
    }


    /**
     * 功能：获取到定时任务的消费服务信息
     * @param app
     * @param targetIp
     * @return
     */
    private EventScheduleConsumerService getScheduleService(String app, String targetIp) {
        EventScheduleConsumerService service = null;
        ReferenceConfig<EventScheduleConsumerService> reference = null;
        if (StringUtils.isEmpty(targetIp)) {
            reference = executeServiceCache.getScheduleService(app);

        }
        //制定目标ip调用的直接调用对应的机器
        else {
            //当没有注册的时候才去注册
            if (!scheduleServiceRegister.hadRegisterDirectService(app, targetIp, EventScheduleConsumerService.class)) {
                scheduleServiceRegister.registerDirectService(app, targetIp, EventScheduleConsumerService.class);
            }
            reference = executeServiceCache.getDirectScheduleService(app, targetIp);
        }
        if (reference != null) {
            service = reference.get();
        }
        return service;
    }

    /**
     * 功能：获取到正常服务的应用
     *
     * @param typeList
     * @return
     */
    private Set<String> getNormalServiceApp(List<EventType> typeList) {
        Set<String> appList = new HashSet<>();
        for (EventType type : typeList) {
            if (type.getSchedule() == 0) {
                appList.add(type.getAppCode());
            }
        }
        return appList;
    }


    /**
     * 功能：获取到定时任务的应用
     *
     * @param typeList
     * @return
     */
    private Set<String> getScheduleServiceApp(List<EventType> typeList) {
        Set<String> appList = new HashSet<>();
        for (EventType type : typeList) {
            if (type.getSchedule() == 1) {
                appList.add(type.getAppCode());
            }
        }
        return appList;
    }


}
