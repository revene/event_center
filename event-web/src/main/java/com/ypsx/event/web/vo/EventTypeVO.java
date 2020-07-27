package com.ypsx.event.web.vo;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能：事件类型的视图对象
 *
 * @author chuchengyi
 */
public class EventTypeVO implements Serializable {


    private static final long serialVersionUID = 378416332422892832L;
    /**
     * 功能：数据ID
     */
    @ApiModelProperty(value = "ID")
    private long id;
    /**
     * 功能：应用标识
     */
    @ApiModelProperty(value = "应用标识")
    private String appCode;

    @ApiModelProperty(value = "类型名称")
    private String name;
    /**
     * 功能：事件的类型
     */
    @ApiModelProperty(value = "类型编码")
    private String eventType;
    /**
     * 功能：最大执行次数
     */
    @ApiModelProperty(value = "执行此次")
    private long maxExecuteSize;

    /**
     * 功能：当前的状态
     */

    @ApiModelProperty(value = "状态 0 创建 1 表示激活")
    private long status;
    /**
     * 功能：执行的调度间隔
     */
    @ApiModelProperty(value = "时间间隔")
    private Long scheduleTime;
    /**
     * 功能：创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
    /**
     * 功能：修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date gmtModify;

    /**
     * 功能：定时任务
     */
    @ApiModelProperty(value = "任务类型 0表示普通事件 1定时事件")
    private Integer schedule;


    /**
     * 功能：定时任务时间表达式
     */
    @ApiModelProperty(value = "定时任务的表达式 参照spring cron", example = "0 0/1 * * * ?")
    private String cronExpression;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Long scheduleTime) {
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

