package com.ypsx.event.error;

/**
 * @author chuchengyi
 */
public interface ExceptionInfo {


    /**
     * 功能：获取错误编码
     *
     * @return
     */
    public String getCode();


    /**
     * 功能：获取一个错误消息
     *
     * @return
     */
    public String getMessage();


}
