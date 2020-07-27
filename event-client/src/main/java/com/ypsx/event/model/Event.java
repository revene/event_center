package com.ypsx.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chuchengyi
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 3786803311680105329L;

    /**
     * 功能:数据ID
     */
    private Long id;

    /**
     * 功能：状态
     *
     * @see EventStatus
     */
    private Integer status;
    /**
     * 功能：执行次数
     */
    private Long executeSize;

    /**
     * 功能：创建时间
     */
    private Date gmtCreate;

    /**
     * 功能：修改时间
     */
    private Date gmtModify;


    /***
     * 功能：执行时间
     */
    private Long executeTime;


    /**
     * 功能：应用标识
     */
    private String appCode;

    /**
     * 功能：事件类型
     */
    private String eventType;

    /**
     * 功能：业务唯一标识
     */
    private String bizId;

    /**
     * 功能：事件的版本号
     */
    private Long version;

    /**
     * 功能：返回调用目标IP
     */
    private String targetIp;


    /**
     * 功能：消息发送的通道
     *
     * @see EventChanel
     */
    private String publishChanel;


    /**
     * 功能更：消息的接受通道
     *
     * @see EventChanel
     */
    private String receiveChanel;

    /**
     * 功能：参数信息
     */
    private Map<String, String> param;


    /**
     * 功能：请求信息最终保存到数据库
     */
    private String request;


    /**
     * 功能：数据索引信息
     */
    private Long dataIndex;


    public Event() {
        this.publishChanel = EventChanel.DUBBO.getCode();
        this.receiveChanel = EventChanel.DUBBO.getCode();
        this.param = new HashMap<>();
    }


    public String getRequest() {
        //讲map转成string
        if (this.param != null && this.param.size() > 0) {
            this.request = JSON.toJSONString(this.param);
        }
        return request;
    }

    public void setRequest(String request) {
        //转成map
        if (request != null) {
            this.param = JSON.parseObject(request, Map.class);
        }
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getExecuteSize() {
        return executeSize;
    }

    public void setExecuteSize(Long executeSize) {
        this.executeSize = executeSize;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getPublishChanel() {
        return publishChanel;
    }

    public void setPublishChanel(String publishChanel) {
        this.publishChanel = publishChanel;
    }

    public String getReceiveChanel() {
        return receiveChanel;
    }

    public void setReceiveChanel(String receiveChanel) {
        this.receiveChanel = receiveChanel;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public Long getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Long dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    /**
     * 功能：添加参数信息
     *
     * @param key
     * @param value
     * @return 返回当前对象
     */
    public Event addParam(String key, String value) {
        this.param.put(key, value);
        return this;
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(status, event.status) &&
                Objects.equals(executeSize, event.executeSize) &&
                Objects.equals(gmtCreate, event.gmtCreate) &&
                Objects.equals(gmtModify, event.gmtModify) &&
                Objects.equals(executeTime, event.executeTime) &&
                Objects.equals(appCode, event.appCode) &&
                Objects.equals(eventType, event.eventType) &&
                Objects.equals(bizId, event.bizId) &&
                Objects.equals(version, event.version) &&
                Objects.equals(targetIp, event.targetIp) &&
                Objects.equals(publishChanel, event.publishChanel) &&
                Objects.equals(receiveChanel, event.receiveChanel) &&
                Objects.equals(param, event.param) &&
                Objects.equals(request, event.request) &&
                Objects.equals(dataIndex, event.dataIndex);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
