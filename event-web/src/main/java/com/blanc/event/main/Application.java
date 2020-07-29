package com.blanc.event.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主程序入口
 *
 * @author blanc
 */
@MapperScan(basePackages = "com.blanc.event.dao")
@EnableDubbo(scanBasePackages = "com.blanc.event.service")
@ComponentScan({"com.blanc.event","com.alibaba.dubbo.config"})
@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
