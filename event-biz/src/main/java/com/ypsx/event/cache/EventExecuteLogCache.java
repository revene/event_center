package com.ypsx.event.cache;

import com.ypsx.event.model.EventLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @author chuchengyi
 */
@Component
public class EventExecuteLogCache extends AbstractEventExecuteCache {

    @Resource
    private JedisPool jedisPool;

    /**
     * 功能：日志缓存的前缀
     */
    private static final String EXECUTE_LOG_PREFIX = "EXECUTE_LOG";

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    /**
     * 功能：判断是否是执行结果
     *
     * @param event
     * @return
     */
    public boolean isExecuteLog(EventLog event) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String key = genLogKey(EXECUTE_LOG_PREFIX, event);
            String result = resource.get(key);
            if (result == null) {
                return false;
            }
        } catch (Throwable throwable) {
            logger.error("EventExecuteLogCache[isExecuteLog] is error:", throwable);
        } finally {
            resource.close();
        }
        return true;
    }


    /**
     * 功能：添加执行结果
     *
     * @param event
     */
    public void addExecuteLog(EventLog event) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String key = genLogKey(EXECUTE_LOG_PREFIX, event);
            String result = resource.set(key, "1","NX","EX",3600);
        } catch (Throwable throwable) {
            logger.error("EventExecuteLogCache[addExecuteLog] is error:", throwable);
        } finally {
            resource.close();
        }
    }


}
