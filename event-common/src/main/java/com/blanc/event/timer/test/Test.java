package com.blanc.event.timer.test;

import com.blanc.event.timer.impl.HashedWheelTimer;
import com.blanc.event.timer.Timer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author chuchengyi
 */
public class Test {

    private static final int SIZE = 100;

    public static void main(String[] args) {
        try {

            final Timer timer = new HashedWheelTimer();

            final CountDownLatch latch = new CountDownLatch(SIZE);
            for (int i = 0; i < SIZE; i++) {
                TestTimerTask task = new TestTimerTask();
                task.setData(i + "");
                task.setCountDownLatch(latch);
                timer.newTimeout(task, i % 5, TimeUnit.SECONDS);

            }
            latch.await();

            timer.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
