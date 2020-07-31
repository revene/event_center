package com.blanc.event.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件执行日志VO
 *
 * @author wangbaoliang
 */
@Data
@ToString
public class EventLogVO implements Serializable {

    private static final long serialVersionUID = 1613581348451961653L;

    /**
     * 数据ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 功能：事件ID
     */
    @ApiModelProperty(value = "事件ID")
    private Long eventId;

    /**
     * 应用代码
     */
    @ApiModelProperty(value = "应用标识")
    private String appCode;

    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型")
    private String eventType;

    /**
     * 功能：事件执行的日志
     */
    @ApiModelProperty(value = "业务ID")
    private String bizId;

    /**
     * 执行事件
     */
    @ApiModelProperty(value = "执行时间")
    private Date executeTime;

    /**
     * 0 成功 -1 失败
     */
    @ApiModelProperty(value = "执行结果 0 成功 -1 失败")
    private Integer success;

    /**
     * 错误消息
     */
    @ApiModelProperty(value = "错误消息")
    private String errorMessage;

    /**
     * 执行IP
     */
    @ApiModelProperty(value = "执行IP")
    private String executeIp;

    /**
     * 功能：创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date gmtModify;

    /**
     * 执行的先后顺序
     */
    @ApiModelProperty(value = "执行顺序")
    private Integer executeIndex;

    /**
     * 功能：开始执行时间
     */
    @ApiModelProperty(value = "开始执行时间")
    private Date startExecute;


    /**
     * 功能：结束执行时间
     */
    @ApiModelProperty(value = "结束执行时间")
    private Date endExecute;


    /**
     * 功能：执行耗时
     */
    @ApiModelProperty(value = "执行耗时")
    private Long costTime;

}