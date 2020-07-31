package com.blanc.event.timer.test;

import com.blanc.event.timer.TaskExecutor;

/**
 * @author chuchengyi
 */
public class TestTaskExecutor implements TaskExecutor<TestTimerTask> {
    @Override
    public boolean executeTask(TestTimerTask testTimerTask) {

        testTimerTask.getCountDownLatch().countDown();

        System.out.println("执行任务：" + testTimerTask.getTask() + " lock=" + testTimerTask.getCountDownLatch().getCount());
        return true;
    }
}
