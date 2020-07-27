package com.blanc.event.model;

/**
 * 事件类型枚举
 */
public enum EventTaskType {

    /**
     * 普通事件:类似于消息队列
     */
    NORMAL_TASK(0, "普通事件"),

    /**
     * 定时事件:提供分布式定时任务
     */
    SCHEDULE_TASK(1, "定时任务");


    private EventTaskType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;

    private String name;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 判断当前事件类型是不是定时任务
     *
     * @param code 事件类型编码
     * @return true or false
     */
    public static boolean isScheduleTask(int code) {
        return SCHEDULE_TASK.getCode() == code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
