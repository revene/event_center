package com.blanc.event.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 事件类型创建请求
 *
 * @author wangbaoliang
 */
@Data
@ToString
public class EventTypeRequest implements Serializable {

    private static final long serialVersionUID = -508027879020342113L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id", example = "1")
    private long id;

    /**
     * 功能：应用标识
     */
    @ApiModelProperty(value = "应用代码", example = "test", required = true)
    private String appCode;

    /**
     * 事件的名称
     */
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

}
