package com.blanc.event.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * 功能：缓存执行成功的标志
 *
 * @author chuchengyi
 */
@Component
public class EventExecuteResultCache extends AbstractEventExecuteCache {

    @Resource
    private JedisPool jedisPool;


    /**
     * 功能：执行的前缀
     */
    private static final String EXECUTE_SUCCESS_PREFIX = "EXECUTE_SUCCESS";

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    /**
     * 功能：判断是否已经执行成功
     *
     * @param eventId
     * @return
     */
    public boolean isSuccessExecuted(Long eventId) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String key = genSuccessKey(EXECUTE_SUCCESS_PREFIX, eventId);
            String result = resource.get(key);
            if (result == null) {
                return false;
            }
        } catch (Throwable throwable) {
            logger.error("EventExecuteResultCache[isSuccessExecuted] is error:", throwable);
        } finally {
            resource.close();
        }
        return true;
    }

    /**
     * 功能：添加已经执行成功标志
     *
     * @param eventId
     */
    public void addSuccessExecuted(Long eventId) {
        if (eventId != null) {
            Jedis resource = null;
            try {
                resource = jedisPool.getResource();
                String key = genSuccessKey(EXECUTE_SUCCESS_PREFIX, eventId);
                String result = resource.set(key, "1", "NX", "EX", 60 * 3);
            } catch (Throwable throwable) {
                logger.error("EventExecuteResultCache[addSuccessExecuted] is error:", throwable);
            } finally {
                resource.close();
            }
        }

    }


}
