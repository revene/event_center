package com.blanc.event.timer.impl;

import com.blanc.event.timer.TimerTask;
import com.blanc.event.timer.test.TestTimerTask;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author ：blanc
 * @date ：Created in 2020/7/31 下午3:25
 */
public class HashedWheelTimerTest {

    @Test
    public void newTimeout() {
        //初始化一个新的时间轮
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();

        //3s后执行
        hashedWheelTimer.newTimeout(new TestTimerTask(), 3000L, TimeUnit.MILLISECONDS);
    }
}