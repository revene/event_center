package com.blanc.event.web.request;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author chuchengyi
 */
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -865475040832453580L;


    @ApiModelProperty(value = "每页的记录数", example = "20")
    protected Integer pageSize;

    @ApiModelProperty(value = "当前页数", example = "1")
    protected Integer pageNumber;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
