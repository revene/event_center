package com.blanc.event.timer;

/**
 * @author chuchengyi
 */
public abstract class AbstractTimerTask<T> implements TimerTask {

    @Override
    public void run(Timeout timeout) throws Exception {
        this.getTaskListener().executeTask(this);
    }

}
