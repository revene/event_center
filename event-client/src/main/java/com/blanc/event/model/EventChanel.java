package com.blanc.event.model;

/**
 * 事件通道枚举
 *
 * @author wangbaoliang
 */
public enum EventChanel {

    /**
     * Dubbo服务通道
     */
    DUBBO("DUBBO", "服务通道"),

    /**
     * 消息通道
     */
    MQ("MQ", "消息通道");

    /**
     * 功能：通道的代码
     */
    private String code;

    /**
     * 功能：通道的名称
     */
    private String name;


    private EventChanel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
