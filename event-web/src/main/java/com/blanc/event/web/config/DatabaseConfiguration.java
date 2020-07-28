package com.blanc.event.web.config;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * sharding 数据源配置
 *
 * @author ：blanc
 * @date ：Created in 2020/7/27 下午11:32
 */
@Configuration
public class DatabaseConfiguration {

    /**
     * 创建读取yml文件的sharding数据源
     *
     * @return sharding数据源
     */
    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        return YamlShardingDataSourceFactory.createDataSource(getFile("/sharding-config.yml"));
    }

    /**
     * 获取配置文件
     *
     * @param configFile 配置文件
     * @return sharding配置文件
     */
    private File getFile(String configFile) {
        return new File(Thread.currentThread().getClass().getResource(configFile).getFile());
    }

}
