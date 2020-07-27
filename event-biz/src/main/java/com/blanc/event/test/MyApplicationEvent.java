package com.blanc.event.test;

import org.springframework.context.ApplicationEvent;

/**
 * 我的测试事件
 *
 * @author ：blanc
 * @date ：Created in 2020/7/23 下午2:55
 */
public class MyApplicationEvent extends ApplicationEvent {

    public MyApplicationEvent(Object source) {
        super(source);
    }
}
