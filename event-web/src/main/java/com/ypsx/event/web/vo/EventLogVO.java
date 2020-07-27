package com.ypsx.event.web.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能：事件执行的日志信息
 *
 * @author chuchengyi
 */
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

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExecuteIp() {
        return executeIp;
    }

    public void setExecuteIp(String executeIp) {
        this.executeIp = executeIp;
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

    public Integer getExecuteIndex() {
        return executeIndex;
    }

    public void setExecuteIndex(Integer executeIndex) {
        this.executeIndex = executeIndex;
    }

    public Date getStartExecute() {
        return startExecute;
    }

    public void setStartExecute(Date startExecute) {
        this.startExecute = startExecute;
    }

    public Date getEndExecute() {
        return endExecute;
    }

    public void setEndExecute(Date endExecute) {
        this.endExecute = endExecute;
    }


    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}