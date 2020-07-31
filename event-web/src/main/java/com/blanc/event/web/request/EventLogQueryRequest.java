package com.blanc.event.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 事件日志查询请求
 *
 * @author wangbaoliang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class EventLogQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = 6206594716573913836L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "数据ID", example = "111")
    private Long id;

    /**
     * 事件id
     */
    @ApiModelProperty(value = "事件ID", example = "1111", required = true)
    private Long eventId;

    /**
     * 功能：业务唯一标识
     */
    @ApiModelProperty(value = "业务ID", example = "SN123213213123", required = true)
    private String bizId;
}
