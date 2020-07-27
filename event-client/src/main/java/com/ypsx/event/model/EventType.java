package com.ypsx.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chuchengyi
 */
public class EventType implements Serializable {

    private static final long serialVersionUID = 5168418112364497670L;
    /**
     * 功能：数据ID
     */
    private long id;
    /**
     * 功能：应用标识
     */
    private String appCode;

    /**
     * 功能：事件类型名称
     */
    private String name;


    /**
     * 功能：事件的类型
     */
    private String eventType;
    /**
     * 功能：最大执行次数
     */
    private long maxExecuteSize;
    /**
     * 功能：当前的状态
     */
    private long status;
    /**
     * 功能：执行的调度间隔
     */
    private long scheduleTime;
    /**
     * 功能：创建时间
     */
    private Date gmtCreate;
    /**
     * 功能：修改时间
     */
    private Date gmtModify;

    /**
     * 功能：定时任务
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
