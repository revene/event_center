package com.ypsx.event.cache;

import com.ypsx.event.model.Event;
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
public class EventExecuteCache extends AbstractEventExecuteCache {

    @Resource
    private JedisPool jedisPool;


    /**
     * 功能：执行的前缀
     */
    private static final String EXECUTE_PREFIX = "EXECUTE";

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    /**
     * 功能：判断是否已经执行
     *
     * @param event
     * @return
     */
    public boolean isExecuted(Event event) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String key = genKey(EXECUTE_PREFIX, event);
            String result = resource.get(key);
            if (result == null) {
                return false;
            }
        } catch (Throwable throwable) {
            logger.error("EventExecuteCache[isExecuted] is error:", throwable);
        } finally {
            resource.close();
        }
        return true;
    }


    /**
     * 功能：添加执行记录
     *
     * @param event
     */
    public boolean addExecute(Event event) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String key = genKey(EXECUTE_PREFIX, event);
            String result = resource.set(key, "1", "NX", "EX", 60 * 3);
            //判断是否是添加成功
            return isStatusOk(result);
        } catch (Throwable throwable) {
            logger.error("EventExecuteCache[addExecute] is error:", throwable);
        } finally {
            resource.close();
        }
        return false;
    }


}
