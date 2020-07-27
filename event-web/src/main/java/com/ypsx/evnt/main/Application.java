package com.ypsx.evnt.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 功能：程序的主要应用
 *
 * @author chuchengyi
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

@EnableScheduling
@EnableAutoConfiguration
@EnableTransactionManagement
@ImportResource(locations = "classpath:data-sharing.xml")
@ComponentScan("com.ypsx.event")
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
