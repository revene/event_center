package com.ypsx.event.web.request;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 功能：事件类型的注册接口
 *
 * @author chuchengyi
 */
public class EventTypeRequest implements Serializable {

    private static final long serialVersionUID = -508027879020342113L;

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    /**
     * 功能：应用标识
     */
    @ApiModelProperty(value = "应用代码", example = "test", required = true)
    private String appCode;

    @ApiModelProperty(value = "事件类型名称", example = "test", required = true)
    private String name;


    /**
     * 功能：事件的类型
     */
    @ApiModelProperty(value = "事件类型", example = "1111", required = true)
    private String eventType;
    /**
     * 功能：最大执行次数
     */
    @ApiModelProperty(value = "最大重试次数", example = "6553566666")
    private long maxExecuteSize;

    /**
     * 功能：定时任务
     */
    @ApiModelProperty(value = "事件类型0 普通任务 1 定时任务", example = "0", required = true)
    private Integer schedule;


    /**
     * 功能：执行的调度间隔
     */
    @ApiModelProperty(value = "普通事件执行的间隔 单位为ms", example = "3000")
    private long scheduleTime;

    /**
     * 功能：定时任务时间表达式
     */
    @ApiModelProperty(value = "定时任务的表达式 参照spring cron", example = "0 0/1 * * * ?")
    private String cronExpression;


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

    public long getMaxExecuteSize() {
        return maxExecuteSize;
    }

    public void setMaxExecuteSize(long maxExecuteSize) {
        this.maxExecuteSize = maxExecuteSize;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
