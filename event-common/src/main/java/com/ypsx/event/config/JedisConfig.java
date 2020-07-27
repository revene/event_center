package com.ypsx.event.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author chuchengyi
 */
@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;




    @Bean("jedisPool")
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        if (StringUtils.isEmpty(password)) {
            password = null;
        }
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);

        logger.info("JedisPool注入成功！ redis地址：" + host + ":" + port);
        return jedisPool;
    }

}