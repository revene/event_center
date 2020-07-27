package com.ypsx.event.sevice;

import com.ypsx.event.listener.EventConsumerListener;
import com.ypsx.event.util.IpUtil;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chuchengyi
 */
public abstract class AbstractEventConsumerService {


    /**
     * 功能：获取系统的ip信息
     */
    protected InetAddress address = IpUtil.getLocalHostDress();


    /**
     * 功能：设置服务信息
     */
    protected Map<String, EventConsumerListener> listenerMap = new ConcurrentHashMap<>();


    /**
     * 功能：添加处理器
     *
     * @param listener
     */
    public void addListener(EventConsumerListener listener) {
        String key = listener.getEventType();
        this.listenerMap.put(key, listener);
    }
}
