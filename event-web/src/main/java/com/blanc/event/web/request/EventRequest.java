package com.blanc.event.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 具体事件请求对象
 *
 * @author wangbaoliang
 */
@Data
@ToString
public class EventRequest implements Serializable {

    private static final long serialVersionUID = -1622302513771385660L;

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

    /**
     * 功能：版本号
     */
    @ApiModelProperty(value = "业务数据版本号", example = "0", required = true)
    private long version;

    /**
     * 功能：返回调用目标IP
     */
    @ApiModelProperty(value = "直接调用的ip 测试环境使用", example = "127.0.0.1")
    private String targetIp;

    /**
     * 功能：参数信息
     */
    @ApiModelProperty(value = "请求参数", example = "{}")
    private Map<String, String> param;

}
