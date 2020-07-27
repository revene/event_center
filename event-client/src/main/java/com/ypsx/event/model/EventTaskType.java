package com.ypsx.event.model;

/**
 * 功能：事件的状态
 *
 * @author chuchengyi
 */

public enum EventTaskType {
    /**
     * 默认激活状态
     */
    NORMAL_TASK(0, "普通事件"),
    /**
     * 某一次执行失败了
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
     * 功能：判断是不是定时任务
     *
     * @param code
     * @return
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
