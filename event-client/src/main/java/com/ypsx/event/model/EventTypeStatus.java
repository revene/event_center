package com.ypsx.event.model;

/**
 * 功能：事件的状态
 *
 * @author chuchengyi
 */

public enum EventTypeStatus {

    /**
     * 默认激活状态
     */
    CREATE(0, "创建状态"),

    /**
     * 某一次执行失败了
     */
    ACTIVE(1, "激活状态");


    private EventTypeStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    private int status;

    private String name;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
