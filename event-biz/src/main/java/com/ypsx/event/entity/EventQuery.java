package com.ypsx.event.entity;

import lombok.Data;
import lombok.ToString;

/**
 * event事件查询对象
 *
 * @author ：blanc
 * @date ：Created in 2020/7/24 下午9:40
 */
@Data
@ToString
public class EventQuery {

    /**
     * 事件的状态
     */
    private int status;

    /**
     * 事件的时间查询范围:起始
     */
    private long startTime;

    /**
     * 事件的时间查询范围:结束
     */
    private Long endTime;

    /**
     * 分页查询的起始偏移量
     */
    private long offset;

    /**
     * 分页查询的单页数量
     */
    private int limit;
}
