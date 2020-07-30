
package com.blanc.event.timer;


/**
 * HashWheelTimeout(时间轮任务的基本单位)的接口
 *
 * @author wangbaoliang
 */
public interface Timeout {

    /**
     * 获取所在的时间轮 HashedWheelTimer
     *
     * @return 时间轮
     */
    Timer timer();

    /**
     * 获取被包装的定时任务
     *
     * @return 被包装的定时任务
     */
    TimerTask task();

    /**
     * 功能：判断是否过期
     *
     * @return true or false
     */
    boolean isExpired();


    /**
     * 功能：判断是否取消
     *
     * @return true or false
     */
    boolean isCancelled();

    /**
     * 功能：取消
     *
     * @return true or false
     */
    boolean cancel();
}
