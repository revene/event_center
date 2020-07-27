package com.ypsx.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件类型对象
 *
 * @author blanc
 */
public class EventType implements Serializable {

    private static final long serialVersionUID = 5168418112364497670L;

    /**
     * 事件类型主键id
     */
    private long id;

    /**
     * 应用标识
     */
    private String appCode;

    /**
     * 事件类型名称
     */
    private String name;

    /**
     * 事件的类型编码,例如:
     * WMS_SYN_REQUIREMENT
     */
    private String eventType;

    /**
     * 最大执行次数
     */
    private long maxExecuteSize;

    /**
     * 这个事件类型的当前状态
     *
     * @see EventTypeStatus
     */
    private long status;

    /**
     * 重试的间隔时间
     */
    private long scheduleTime;

    /**
     * 事件创建时间
     */
    private Date gmtCreate;

    /**
     * 最近修改时间
     */
    private Date gmtModify;

    /**
     * 是否是定时任务
     * 1: 是定时任务事件
     * 2: 普通事件
     */
    private Integer schedule;

    /**
     * 功能：定时任务时间表达式
     */
    private String cronExpression;


    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getMaxExecuteSize() {
        return maxExecuteSize;
    }

    public void setMaxExecuteSize(long maxExecuteSize) {
        this.maxExecuteSize = maxExecuteSize;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
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


    public Integer getSchedule() {
        return schedule;
    }

    public void setSchedule(Integer schedule) {
        this.schedule = schedule;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
