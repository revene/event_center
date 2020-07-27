package com.ypsx.event.model;

import java.io.Serializable;

/**
 * 功能：分页查询
 *
 * @author chuchengyi
 */
public class PageQuery implements Serializable {


    private static final long serialVersionUID = -2409885120965772918L;

    /**
     * 功能：每一页的数据记录
     */
    protected Integer pageSize;


    /**
     * 功能：页码
     */
    protected Integer pageNumber;


    /**
     * 功能：开始记录
     */
    protected Integer start;


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


    public Integer getStart() {
        if (start == null) {
            if (pageSize != null && pageNumber != null) {
                int temp = (pageNumber - 1) * pageSize;
                if (temp < 0) {
                    temp = 0;
                }
                return temp;
            }
        }
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
