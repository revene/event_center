package com.blanc.event.client.starter.annotation;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.blanc.event.sevice.ConsumerService;
import com.blanc.event.sevice.EventPublishService;
import com.blanc.event.sevice.EventScheduleConsumerService;
import com.blanc.event.sevice.impl.EventScheduleServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

/**
 * 功能：定时任务消费服务启动探测
 *
 * @author chuchengyi
 */
@Configuration
public class ScheduleEventConsumerAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {


    /**
     * 功能：定义了工厂类
     */
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        EventConfig config = getAnnotation(bean.getClass(), EventConfig.class);
        if (config != null) {
            //通过字段的方式去注册消费服务
            registerConsumerEventByField(bean);
            //通过方法的方式注册消费服务
            registerConsumerEventMethod(bean);
        }
        return bean;
    }


    /**
     * 功能：通过字段的方式进行注册
     *
     * @param bean
     */
    private void registerConsumerEventByField(Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ScheduleEventConsumer eventConsumer = getAnnotation(field, ScheduleEventConsumer.class);
                if (eventConsumer != null) {
                    EventScheduleConsumerService service = new EventScheduleServiceImpl();
                    if (service != null) {
                        registerServer(eventConsumer, service);
                    }
                    //设置成可以访问
                    field.setAccessible(true);
                    //设置成直接赋值
                    field.set(bean, service);
                }
            }
        });

    }


    /**
     * 功能：通过方法的方式进行注册
     *
     * @param bean
     */
    private void registerConsumerEventMethod(Object bean) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            //获取到定时事件消费程序的注解
            ScheduleEventConsumer eventConsumer = EventConsumerAnnotationHelper.determineScheduleEventConsumer(method);
            //当注解不为空的情况下
            if (eventConsumer != null) {
                //获取到服务
                EventScheduleConsumerService service = getService(method, bean);
                if (service != null) {
                    registerServer(eventConsumer, service);
                }
            }
        }

    }

    /**
     * 功能：获取方法调用的值
     *
     * @param method
     * @param bean
     * @return
     */
    private EventScheduleConsumerService getService(Method method, Object bean) {
        try {
            method.setAccessible(true);
            Object service = method.invoke(bean);
            if (service instanceof EventScheduleConsumerService) {
                EventScheduleConsumerService eventConsumerService = (EventScheduleConsumerService) service;
                return eventConsumerService;
            }
        } catch (Throwable throwable) {
            new RuntimeException(throwable.getMessage());
        }
        return null;
    }


    /**
     * 功能：注册事件信息消费服务
     *
     * @param eventConsumer
     * @param eventConsumerService
     */
    private void registerServer(ScheduleEventConsumer eventConsumer, EventScheduleConsumerService eventConsumerService) {
        ServiceConfig<EventScheduleConsumerService> serviceConfig = new ServiceConfig<EventScheduleConsumerService>();
        try {
            //获取到应用的信息
            ApplicationConfig application = this.beanFactory.getBean(ApplicationConfig.class);
            //获取到注册地址信息
            RegistryConfig registry = this.beanFactory.getBean(RegistryConfig.class);
            //获取到协议信息
            ProtocolConfig protocol = this.beanFactory.getBean(ProtocolConfig.class);

            //设置应用标识
            serviceConfig.setApplication(application);
            // 多个注册中心可以用setRegistries()
            serviceConfig.setRegistry(registry);
            // 多个协议可以用setProtocols()
            serviceConfig.setProtocol(protocol);
            serviceConfig.setInterface(EventScheduleConsumerService.class);
            //设置实现类
            serviceConfig.setRef(eventConsumerService);
            //版本号各个系统不同
            serviceConfig.setVersion(eventConsumer.version());
            //设置超时时间
            serviceConfig.setTimeout(eventConsumer.timeout());
            // 暴露及注册服务
            serviceConfig.export();

            //如果没有指定需要从工厂里面获取
            if (eventConsumerService.getEventPublishService() == null) {
                //设置事件发布服务
                EventPublishService eventPublishService = this.beanFactory.getBean(EventPublishService.class);
                //设置发布服务
                eventConsumerService.setEventPublishService(eventPublishService);
            }
            //初始化
            ConsumerService initializingBean = (ConsumerService) eventConsumerService;
            initializingBean.startConsumer();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            new RuntimeException(throwable.getMessage());
        }
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
