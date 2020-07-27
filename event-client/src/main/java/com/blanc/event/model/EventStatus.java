package com.blanc.event.model;

/**
 * 事件的状态枚举
 *
 * @author wangbaoliang
 */
public enum EventStatus {

    /**
     * 默认激活状态
     */
    ACTIVE(0, "激活状态"),

    /**
     * 某一次执行失败了
     */
    EXECUTE_FAIL(1, "执行失败"),

    /**
     * 执行成功了
     */
    SUCCESS(2, "执行成功"),

    /**
     * 多次执行最终失败了
     */
    FAIL(3, "最终失败"),

    /**
     * 取消了
     */
    CANCEL(-1, "取消");


    private EventStatus(int status, String name) {
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
