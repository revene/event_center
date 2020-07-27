package com.blanc.event.service.register;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.blanc.event.cache.ExecuteServiceCache;
import com.blanc.event.sevice.EventConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chuchengyi
 */
@Component
public class NormalConsumerServiceRegister extends AbstractConsumerServiceRegister implements ConsumerServiceRegister<EventConsumerService> {


    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("registerLog");

    /**
     * 功能：执行服务的缓存
     */
    private ExecuteServiceCache executeServiceCache = ExecuteServiceCache.getInstance();


    /**
     * 功能：设置方法调用的异步处理机制
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
        consumerEventConfig.setName("consumerEvent");
        //设置正常返回的处理对象
        consumerEventConfig.setOnreturn(this.normalConsumerServiceHandler);
        //设置正常返回的方法
        consumerEventConfig.setOnreturnMethod("processResult");
        //设置抛出异常处理的对象
        consumerEventConfig.setOnthrow(this.normalConsumerServiceHandler);
        //设置处理异常的方法
        consumerEventConfig.setOnthrowMethod("processException");
        methodList.add(consumerEventConfig);
        return methodList;
    }


    @Override
    public void registerService(String app, Class<EventConsumerService> serviceClass) {

        try {
            // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            ReferenceConfig<EventConsumerService> reference = new ReferenceConfig<EventConsumerService>();
            reference.setApplication(applicationConfig);
            // 多个注册中心可以用setRegistries()
            reference.setRegistry(registryConfig);
            reference.setInterface(serviceClass.getName());
            reference.setVersion(app);

            List<MethodConfig> methodList = initMethodList();
            reference.setMethods(methodList);
            EventConsumerService service = reference.get();
            executeServiceCache.addService(app, reference);
            logger.error("NormalServiceRegister[registerService] register success :" + serviceClass.getName() + " version=" + app);
        } catch (Throwable throwable) {
            throwable.getMessage();
            logger.error("NormalConsumerServiceRegister[registerService] init service is error  app=" + app, throwable);
        }
    }

    @Override
    public void registerDirectService(String app, String ip, Class<EventConsumerService> serviceClass) {

        try {
            // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            ReferenceConfig<EventConsumerService> reference = new ReferenceConfig<EventConsumerService>();
            String className = serviceClass.getName();
            reference.setUrl("dubbo://" + ip + ":20880/" + className);
            //初始化异步调用的方法信息
            List<MethodConfig> methodList = initMethodList();
            //设置异步调用的方法
            reference.setMethods(methodList);
            EventConsumerService service = reference.get();
            executeServiceCache.addDirectService(app, ip, reference);
        } catch (Throwable throwable) {
            logger.error("NormalConsumerServiceRegister[registerDirectService] init service is error app" + app + "  ip=" + ip, throwable);
        }

    }


    @Override
    public boolean hadRegisterService(String app, Class<EventConsumerService> serviceClass) {
        return executeServiceCache.existService(app);
    }

    @Override
    public boolean hadRegisterDirectService(String app, String ip, Class<EventConsumerService> serviceClass) {
        return executeServiceCache.existDirectScheduleService(app, ip);
    }
}
