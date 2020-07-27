package com.blanc.event.model;

import java.io.Serializable;

/**
 * 功能：事件执行的结果
 *
 * @author chuchengyi
 */
public class EventExecuteResult implements Serializable {

    private static final long serialVersionUID = 4372534017387548830L;

    /**
     * 功能：事件的内容
     */
    private Event event;

    /**
     * 功能：事件执行的结果
     */
    private EventResult result;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EventResult getResult() {
        return result;
    }

    public void setResult(EventResult result) {
        this.result = result;
    }


    /**
     * 功能:手动释放数据 不然会一直在内容中保持强引用信息
     */
    public void clear() {
        //手动释放事件信息
        this.event = null;
        //手动释放执行结果
        this.result = null;
    }
}
