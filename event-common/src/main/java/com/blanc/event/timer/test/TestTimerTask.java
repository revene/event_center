package com.blanc.event.timer.test;

import com.blanc.event.timer.TaskExecutor;
import com.blanc.event.timer.Timeout;
import com.blanc.event.timer.TimerTask;

import java.util.concurrent.CountDownLatch;

/**
 * @author chuchengyi
 */
public class TestTimerTask implements TimerTask {


    private String data;

    TestTaskExecutor taskListener = new TestTaskExecutor();

    private CountDownLatch countDownLatch;

    @Override
    public void run(Timeout timeout) throws Exception {

    }

    @Override
    public String getTask() {
        return data;
    }

    @Override
    public TaskExecutor getTaskExecutor() {
        return taskListener;
    }

    @Override
    public String getId() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
