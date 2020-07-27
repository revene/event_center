package com.blanc.event.timer;

/**
 * 功能：事件的处理器
 *
 * @author chuchengyi
 */
public interface TaskListener<T extends TimerTask> {


    /**
     * 功能：执行具体的任务
     *
     * @param timerTask
     * @return
     */
    public boolean executeTask(T timerTask);

}
