package com.ypsx.event.timer.test;

import com.ypsx.event.timer.AbstractTimerTask;
import com.ypsx.event.timer.TaskListener;
import com.ypsx.event.timer.TimerTask;

import java.util.concurrent.CountDownLatch;

/**
 * @author chuchengyi
 */
public class TestTask extends AbstractTimerTask<String> implements TimerTask {


    private String data;

    TestTaskListener taskListener = new TestTaskListener();

    private CountDownLatch countDownLatch;

    @Override
    public String getTask() {
        return data;
    }

    @Override
    public TaskListener getTaskListener() {
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
