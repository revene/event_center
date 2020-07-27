package com.blanc.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author chuchengyi
 */
public class EventQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 292817312352969741L;


    private Long id;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
