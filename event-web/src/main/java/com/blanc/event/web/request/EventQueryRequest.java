package com.blanc.event.web.request;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 功能：事件信息的保存对象
 *
 * @author chuchengyi
 */
public class EventQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = -9193719007890354220L;

    @ApiModelProperty(value = "数据ID", example = "111")
    private Long id;
    /**
     * 功能：应用标识
     */
    @ApiModelProperty(value = "应用代码", example = "test", required = true)
    private String appCode;

    /**
     * 功能：事件类型
     */
    @ApiModelProperty(value = "事件类型", example = "1111", required = true)
    private String eventType;

    /**
     * 功能：业务唯一标识
     */
    @ApiModelProperty(value = "业务ID", example = "SN123213213123", required = true)
    private String bizId;

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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
