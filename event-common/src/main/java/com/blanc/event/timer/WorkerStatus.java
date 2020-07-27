package com.blanc.event.timer;

/**
 * 扫描事件工作线程的状态枚举
 *
 * @author blanc
 */
public enum WorkerStatus {

    /**
     * 初始化状态
     */
    INIT(0, "INIT"),

    /**
     * 启动状态
     */
    STARTED(1, "STARTED"),

    /**
     * 终止关停状态
     */
    SHUTDOWN(2, "SHUTDOWN");

    /**
     * 状态值
     */
    private int status;

    /**
     * 状态描述
     */
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

    WorkerStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }


}
