package com.blanc.event.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 分页请求参数
 *
 * @author wangbaoliang
 */
@Getter
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -865475040832453580L;

    @ApiModelProperty(value = "每页的记录数", example = "20")
    protected Integer pageSize;

    @ApiModelProperty(value = "当前页数", example = "1")
    protected Integer pageNumber;

}
