package com.ypsx.event.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件消费结果
 *
 * @author blanc
 */
public class EventResult implements Serializable {

    private static final long serialVersionUID = -5080434235070838656L;

    /**
     * 事件是否执行成功
     */
    private boolean success;

    /**
     * 功能：设置是否需要需要重试 默认为true success 为true的时候不起作用 当为false 如果需要retry系统不会在重复推送
     */
    private boolean retry;

    /**
     * 功能：下次执行时间 如果不设置 系统会根据事件类型定义的间隔时间+当前时间去执行 如果设置时间会按你设定的时间去执行
     */
    private Long nextExecuteTime;

    /**
     * 功能:调用客户端的ip
     */
    private String clientIp;

    /**
     * 功能：执行的错误信息
     */
    private String errorMessage;

    /**
     * 功能：开始执行时间
     */
    private Date startExecute;

    /**
     * 功能：结束执行时间
     */
    private Date endExecute;

    /**
     * 默认的构造函数
     */
    public EventResult() {
        //默认设置为true
        this.retry = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public Long getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Long nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStartExecute() {
        return startExecute;
    }

    public void setStartExecute(Date startExecute) {
        this.startExecute = startExecute;
    }

    public Date getEndExecute() {
        return endExecute;
    }

    public void setEndExecute(Date endExecute) {
        this.endExecute = endExecute;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
