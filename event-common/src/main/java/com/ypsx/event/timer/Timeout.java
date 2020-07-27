
package com.ypsx.event.timer;


/**
 * 功能：超时数据接口的定义
 *
 * @author chuchengyi
 */
public interface Timeout {


    /**
     * 功能：返回时间轮对象
     *
     * @return
     */
    Timer timer();

    /**
     * 功能：返回具体执行任务对象
     *
     * @return
     */
    TimerTask task();

    /**
     * 功能：判断是否过期
     *
     * @return
     */
    boolean isExpired();


    /**
     * 功能：判断是否取消
     *
     * @return
     */
    boolean isCancelled();

    /**
     * 功能：取消
     *
     * @return
     */
    boolean cancel();
}
