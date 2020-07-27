package com.ypsx.event.cache;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventLog;

/**
 * 功能：缓存抽象类
 *
 * @author chuchengyi
 */
public abstract class AbstractEventExecuteCache {


    private static final String KEY_LINK = "_";

    private static final String OK_CODE = "OK";

    private static final String OK_MULTI_CODE = "+OK";


    /**
     * 判断 返回值是否ok.
     */
    public boolean isStatusOk(String status) {
        //当结果不为空的时候直接转成大写
        if (status != null) {
            //转成大写的
            status = status.toUpperCase();
        }
        return (status != null) && (OK_CODE.equals(status) || OK_MULTI_CODE.equals(status));
    }

    /**
     * 功能：生成一个key
     *
     * @param prefix
     * @param event
     * @return
     */
    public String genKey(String prefix, Event event) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(KEY_LINK);
        buffer.append(event.getId());
        buffer.append(KEY_LINK);
        buffer.append(event.getExecuteSize());
        return buffer.toString();
    }


    /**
     * 功能：生成一个key
     *
     * @param prefix
     * @param event
     * @return
     */
    public String genLogKey(String prefix, EventLog event) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(KEY_LINK);
        buffer.append(event.getId());
        buffer.append(KEY_LINK);
        buffer.append(event.getExecuteIndex());
        return buffer.toString();
    }


    /**
     * 功能：生成一个key
     *
     * @param prefix
     * @param eventId
     * @return
     */
    public String genSuccessKey(String prefix, Long eventId) {
        //定义数据缓存执行成功的标志
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(KEY_LINK);
        buffer.append(eventId);
        return buffer.toString();
    }
}
