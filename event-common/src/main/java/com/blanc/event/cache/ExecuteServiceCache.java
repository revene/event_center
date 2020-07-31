package com.blanc.event.cache;

import com.alibaba.dubbo.config.ReferenceConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chuchengyi
 */
public class ExecuteServiceCache {


    private static final String KEY_LINK = "_";

    /**
     * 单例
     */
    private static final ExecuteServiceCache INSTANCE = new ExecuteServiceCache();

    /**
     * 功能：服务列表信息
     */
    private Map<String, ReferenceConfig> serviceMap = new ConcurrentHashMap<>();

    /**
     * 功能：定时调用的服务列表
     */
    private Map<String, ReferenceConfig> scheduleServiceMap = new ConcurrentHashMap<>();

    /**
     * 功能:直接调用的服务列表 key=app+ip
     */
    private Map<String, ReferenceConfig> directServiceMap = new ConcurrentHashMap<>();


    /**
     * 功能:直接调用定时服务的服务列表 key=app+ip
     */
    private Map<String, ReferenceConfig> scheduleDirectServiceMap = new ConcurrentHashMap<>();

    private ExecuteServiceCache() {

    }

    public static ExecuteServiceCache getInstance() {
        return INSTANCE;
    }


    /**
     * 功能：添加调用的服务信息
     *
     * @param app
     * @param service
     */
    public void addService(String app, ReferenceConfig service) {

        this.serviceMap.put(app, service);
    }

    /**
     * 功能：添加定时服务
     *
     * @param app
     * @param service
     */
    public void addScheduleService(String app, ReferenceConfig service) {
        this.scheduleServiceMap.put(app, service);
    }


    /**
     * 功能：添加直接调用的服务信息
     *
     * @param app
     * @param ip
     * @param service
     */
    public void addDirectService(String app, String ip, ReferenceConfig service) {
        String key = genDirectKey(app, ip);
        this.directServiceMap.put(key, service);
    }


    /**
     * 功能：添加定时服务制定机器调用
     *
     * @param app
     * @param ip
     * @param service
     */
    public void addScheduleDirectService(String app, String ip, ReferenceConfig service) {
        String key = genDirectKey(app, ip);
        this.scheduleDirectServiceMap.put(key, service);
    }


    /**
     * 功能：生成直接调用库存
     *
     * @param app
     * @param ip
     * @return
     */
    private String genDirectKey(String app, String ip) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(app);
        buffer.append(KEY_LINK);
        buffer.append(ip);
        buffer.append(KEY_LINK);
        return buffer.toString();
    }

    /**
     * 功能：获取到dubbo调用的服务
     *
     * @param app
     * @return
     */
    public ReferenceConfig getService(String app) {
        return serviceMap.get(app);
    }


    /**
     * 功能：获取到定时dubbo调用的服务
     *
     * @param app
     * @return
     */
    public ReferenceConfig getScheduleService(String app) {
        return scheduleServiceMap.get(app);
    }

    /**
     * 功能：获取直接调用库存
     *
     * @param app
     * @param targetIp
     * @return
     */
    public ReferenceConfig getDirectService(String app, String targetIp) {
        String key = genDirectKey(app, targetIp);
        return directServiceMap.get(key);
    }


    /**
     * 功能：判断是是否已经存在
     *
     * @param app
     * @param targetIp
     * @return
     */
    public boolean existDirectService(String app, String targetIp) {
        String key = genDirectKey(app, targetIp);
        return directServiceMap.containsKey(key);
    }


    /**
     * 功能：判断是是否已经存在
     *
     * @param app
     * @param targetIp
     * @return
     */
    public boolean existDirectScheduleService(String app, String targetIp) {
        String key = genDirectKey(app, targetIp);
        return scheduleDirectServiceMap.containsKey(key);
    }


    /**
     * 功能：获取直接调用库存
     *
     * @param app
     * @param targetIp
     * @return
     */
    public ReferenceConfig getDirectScheduleService(String app, String targetIp) {
        String key = genDirectKey(app, targetIp);
        return scheduleDirectServiceMap.get(key);
    }

    /**
     * 功能：判断某个服务是否已经存在
     *
     * @param app
     * @return
     */
    public boolean existService(String app) {
        return serviceMap.containsKey(app);
    }

    /**
     * 功能：判断定时任务是否已经存在
     *
     * @param app
     * @return
     */
    public boolean existScheduleService(String app) {
        return scheduleServiceMap.containsKey(app);
    }
}
