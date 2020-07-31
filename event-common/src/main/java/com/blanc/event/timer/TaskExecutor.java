package com.blanc.event.timer;

/**
 * Task处理执行器
 *
 * @param <T> TimerTask实现类
 * @author wangbaoliang
 */
public interface TaskExecutor<T extends TimerTask> {


    /**
     * 功能：执行具体的任务
     *
     * @param timerTask 要执行的任务
     * @return true or false
     */
    boolean executeTask(T timerTask);

}
