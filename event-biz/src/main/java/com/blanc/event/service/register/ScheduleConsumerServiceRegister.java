package com.blanc.event.service.register;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.blanc.event.sevice.EventScheduleConsumerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chuchengyi
 */
@Component
public class ScheduleConsumerServiceRegister extends AbstractConsumerServiceRegister implements ConsumerServiceRegister<EventScheduleConsumerService> {


    /**
     * 功能：设置方法调用的异步处理机制 在调用的时候直接更新下次调用的时间
     *
     * @return
     */
    public List<MethodConfig> initMethodList() {
        List<MethodConfig> methodList = new ArrayList<>();
        //设置事件消费接口的
        MethodConfig consumerEventConfig = new MethodConfig();
        //设置异步调用
        consumerEventConfig.setAsync(true);
        //设置方法名称
        consumerEventConfig.setName("consumerScheduleEvent");
        //设置正常返回的处理对象
        consumerEventConfig.setOninvoke(this.scheduleConsumerServiceHandler);
        //设置正常返回的方法
        consumerEventConfig.setOninvokeMethod("process");
        //设置抛出异常处理的对象
        consumerEventConfig.setOnthrow(this.scheduleConsumerServiceHandler);
        //设置处理异常的方法
        consumerEventConfig.setOnthrowMethod("processException");
        methodList.add(consumerEventConfig);
        return methodList;
    }


    @Override
    public void registerService(String app, Class<EventScheduleConsumerService> serviceClass) {
        try {
            // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            ReferenceConfig<EventScheduleConsumerService> reference = new ReferenceConfig<EventScheduleConsumerService>();
            reference.setApplication(applicationConfig);
            // 多个注册中心可以用setRegistries()
            reference.setRegistry(registryConfig);
            reference.setInterface(serviceClass.getName());
            reference.setVersion(app);

            List<MethodConfig> methodList = initMethodList();
            reference.setMethods(methodList);
            EventScheduleConsumerService service = reference.get();
            executeServiceCache.addScheduleService(app, reference);
        } catch (Throwable throwable) {
            logger.error("ScheduleConsumerServiceRegister[registerService] init service is error app" + app, throwable);
        }


    }

    @Override
    public void registerDirectService(String app, String ip, Class<EventScheduleConsumerService> serviceClass) {
        try {
            // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            ReferenceConfig<EventScheduleConsumerService> reference = new ReferenceConfig<EventScheduleConsumerService>();
            String className = serviceClass.getName();
            reference.setUrl("dubbo://" + ip + ":20880/" + className);
            //初始化异步调用的方法信息
            List<MethodConfig> methodList = initMethodList();
            //设置异步调用的方法
            reference.setMethods(methodList);
            EventScheduleConsumerService service = reference.get();
            executeServiceCache.addScheduleDirectService(app, ip, reference);
        } catch (Throwable throwable) {
            logger.error("ScheduleConsumerServiceRegister[initDirectService] init service is error app" + app + "  ip=" + ip, throwable);
        }
    }

    @Override
    public boolean hadRegisterService(String app, Class<EventScheduleConsumerService> serviceClass) {
        return executeServiceCache.existScheduleService(app);
    }

    @Override
    public boolean hadRegisterDirectService(String app, String ip, Class<EventScheduleConsumerService> serviceClass) {
        return executeServiceCache.existDirectScheduleService(app, ip);
    }
}
