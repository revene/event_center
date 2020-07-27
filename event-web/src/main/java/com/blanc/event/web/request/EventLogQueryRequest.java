package com.blanc.event.web.request;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 功能：事件信息的保存对象
 *
 * @author chuchengyi
 */
public class EventLogQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = 6206594716573913836L;



    @ApiModelProperty(value = "数据ID", example = "111")
    private Long id;

    /**
     * 功能：事件类型
     */
    @ApiModelProperty(value = "事件ID", example = "1111", required = true)
    private Long eventId;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
