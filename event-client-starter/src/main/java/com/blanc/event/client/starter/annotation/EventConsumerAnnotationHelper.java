package com.blanc.event.client.starter.annotation;

import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author chuchengyi
 */
public class EventConsumerAnnotationHelper {


    /**
     * 功能：判断方法是否有注册
     *
     * @param method
     * @return
     */
    public static boolean isEventConsumerAnnotated(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, EventConsumer.class);
    }


    /**
     * 功能：判断字段是否有注册
     *
     * @param field
     * @return
     */
    public static boolean isEventConsumerAnnotated(Field field) {
        return AnnotatedElementUtils.hasAnnotation(field, EventConsumer.class);
    }


    /**
     * 功能：获取注册信息
     *
     * @param field
     * @return
     */
    public static EventConsumer determineEventConsumer(Field field) {

        EventConsumer bean = AnnotatedElementUtils.findMergedAnnotation(field, EventConsumer.class);

        return bean;
    }

    /**
     * 功能：获取注解信息
     *
     * @param beanMethod
     * @return
     */
    public static EventConsumer determineEventConsumer(Method beanMethod) {
        EventConsumer bean = AnnotatedElementUtils.findMergedAnnotation(beanMethod, EventConsumer.class);
        return bean;
    }


    /**
     * 功能：判断方法是否有注册
     *
     * @param method
     * @return
     */
    public static boolean isScheduleEventConsumerAnnotated(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, ScheduleEventConsumer.class);
    }

    /**
     * 功能：货到定时任务消费的注册
     *
     * @param beanMethod
     * @return
     */
    public static ScheduleEventConsumer determineScheduleEventConsumer(Method beanMethod) {
        ScheduleEventConsumer bean = AnnotatedElementUtils.findMergedAnnotation(beanMethod, ScheduleEventConsumer.class);
        return bean;
    }
}
