package com.blanc.event.main;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主程序入口
 *
 * @author blanc
 */
@MapperScan(basePackages = "com.blanc.event.dao")
@ComponentScan({"com.blanc.event","com.alibaba.dubbo.config"})
@EnableScheduling
@DubboComponentScan(basePackages = "com.blanc.event.service")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
