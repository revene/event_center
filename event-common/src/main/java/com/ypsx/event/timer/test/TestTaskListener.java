package com.ypsx.event.timer.test;

import com.ypsx.event.timer.TaskListener;

/**
 * @author chuchengyi
 */
public class TestTaskListener implements TaskListener<TestTask> {
    @Override
    public boolean executeTask(TestTask testTask) {

        testTask.getCountDownLatch().countDown();

        System.out.println("执行任务：" + testTask.getTask() + " lock=" + testTask.getCountDownLatch().getCount());
        return true;
    }
}
