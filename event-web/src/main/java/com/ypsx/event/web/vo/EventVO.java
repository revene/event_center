package com.ypsx.event.web.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ypsx.event.model.EventChanel;
import com.ypsx.event.model.EventStatus;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author chuchengyi
 */
public class EventVO implements Serializable {

    private static final long serialVersionUID = -1774995205442532208L;
    /**
     * 功能:数据ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 功能：状态
     *
     * @see EventStatus
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 功能：执行次数
     */
    @ApiModelProperty(value = "执行的次数")
    private Long executeSize;

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


    /***
     * 功能：执行时间
     */
    @ApiModelProperty(value = "下次执行时间")
    private Long executeTime;


    /**
     * 功能：应用标识
     */

    @ApiModelProperty(value = "应用编码")
    private String appCode;

    /**
     * 功能：事件类型
     */
    @ApiModelProperty(value = "事件类型编码")
    private String eventType;

    /**
     * 功能：业务唯一标识
     */
    @ApiModelProperty(value = "业务ID")
    private String bizId;

    /**
     * 功能：事件的版本号
     */

    @ApiModelProperty(value = "业务版本号")
    private Long version;

    /**
     * 功能：返回调用目标IP
     */
    @ApiModelProperty(value = "制定调用的机器ip")
    private String targetIp;


    /**
     * 功能：消息发送的通道
     *
     * @see EventChanel
     */
    @ApiModelProperty(value = "消息的发布通道")
    private String publishChanel;


    /**
     * 功能更：消息的接受通道
     *
     * @see EventChanel
     */
    @ApiModelProperty(value = "消息接受的通道")
    private String receiveChanel;

    /**
     * 功能：参数信息
     */
    @ApiModelProperty(value = "事件参数信息")
    private Map<String, String> param;


    /**
     * 功能：请求信息最终保存到数据库
     */
    private String request;


    /**
     * 功能：数据索引信息
     */
    private Long dataIndex;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getExecuteSize() {
        return executeSize;
    }

    public void setExecuteSize(Long executeSize) {
        this.executeSize = executeSize;
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

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getPublishChanel() {
        return publishChanel;
    }

    public void setPublishChanel(String publishChanel) {
        this.publishChanel = publishChanel;
    }

    public String getReceiveChanel() {
        return receiveChanel;
    }

    public void setReceiveChanel(String receiveChanel) {
        this.receiveChanel = receiveChanel;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Long getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Long dataIndex) {
        this.dataIndex = dataIndex;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
