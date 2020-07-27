package com.blanc.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * 功能：属性的配置信息
 *
 * @author chuchengyi
 */
@Configuration
public class PropertiesConfig {

//    @Primary
//    @Bean
//    public PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() {
//        ClassPathResource resource = new ClassPathResource("application.properties");
//        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//        propertyPlaceholderConfigurer.setLocation(resource);
//        return propertyPlaceholderConfigurer;
//    }
}
