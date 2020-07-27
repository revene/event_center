package com.blanc.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 功能：事件信息的保存对象
 *
 * @author chuchengyi
 */
public class EventLogQuery extends PageQuery implements Serializable {


    private static final long serialVersionUID = -2464167913953596997L;

    /**
     * 功能：数据ID
     */
    private Long id;

    /**
     * 功能：事件类型
     */

    private Long eventId;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
