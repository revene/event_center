package com.blanc.event.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 事件的query查询请求
 *
 * @author wangbaoliang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class EventQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -9193719007890354220L;

    /**
     * 主键id
     */
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

}
