package com.ypsx.event.web.request;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 功能：事件类型的注册接口
 *
 * @author chuchengyi
 */
public class EventTypeUpdateRequest implements Serializable {

    private static final long serialVersionUID = -4554985948295376708L;
    /**
     * 功能：数据ID
     */
    @ApiModelProperty(value = "数据ID", example = "test", required = true)
    private Long id;


    @ApiModelProperty(value = "事件名称", example = "test", required = true)
    private String name;

    /**
     * 功能：最大执行次数
     */
    @ApiModelProperty(value = "最大重试次数", example = "6553566666",required = true)
    private Long maxExecuteSize;

    /**
     * 功能：执行的调度间隔
     */
    @ApiModelProperty(value = "普通事件执行的间隔 单位为ms", example = "3000")
    private Long scheduleTime;

    /**
     * 功能：定时任务时间表达式
     */
    @ApiModelProperty(value = "定时任务的表达式 参照spring cron", example = "0 0/1 * * * ?")
    private String cronExpression;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaxExecuteSize() {
        return maxExecuteSize;
    }

    public void setMaxExecuteSize(Long maxExecuteSize) {
        this.maxExecuteSize = maxExecuteSize;
    }

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
