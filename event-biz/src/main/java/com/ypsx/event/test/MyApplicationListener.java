package com.ypsx.event.test;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

/**
 * 我的监听application器
 *
 * @author ：blanc
 * @date ：Created in 2020/7/23 下午2:14
 */
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationContextEvent> {

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        System.out.println("收到监听事件--------------------------------------------------------");
    }
}
