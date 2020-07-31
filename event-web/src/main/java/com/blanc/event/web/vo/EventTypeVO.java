package com.blanc.event.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件类型VO
 *
 * @author wangbaoliang
 */
@Data
@ToString
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

    /**
     * 类型名称
     */
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

}

