package com.blanc.event.timer.impl;

import com.blanc.event.timer.Timeout;
import com.blanc.event.timer.WorkerStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 功能：时间轮的调度线程
 *
 * @author blanc
 */
@Slf4j(topic = "timer")
public class DispatchWorker implements Runnable {


    /**
     * 时间tick指针
     */
    private long tick;

    /**
     * 启动时间
     */
    private long startTime;

    /**
     * 时间轮
     */
    private HashedWheelTimer timer;

    /**
     * 功能：当前线程的状态
     */
    private volatile int workerState;

    /**
     * 功能：状态字段的名称
     */
    private static final String STATE_NAME = "workerState";

    /**
     * 功能：未处理列表
     */
    private final Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();

    /**
     *
     */
    private static final int DISPATCH_SIZE = 100000;

    /**
     * 状态cas修改器
     */
    private static final AtomicIntegerFieldUpdater<DispatchWorker> WORKER_STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(DispatchWorker.class, STATE_NAME);

    /**
     * 构造器,一个时间轮一个DispatchWorker
     *
     * @param timer 时间轮
     */
    public DispatchWorker(HashedWheelTimer timer) {
        this.timer = timer;
    }

    /**
     * 设置当前调度线程为启动状态
     */
    public void start() {
        int initStatus = WorkerStatus.INIT.getStatus();
        int startStatus = WorkerStatus.STARTED.getStatus();
        while (true) {
            //判断一下是否是启动了
            if (isStart()) {
                break;
            }
            //尝试设置成启动装
            boolean start = WORKER_STATE_UPDATER.compareAndSet(this, initStatus, startStatus);
            if (start) {
                break;
            }
        }
        log.info("DispatchWorker[start] is success!");
    }

    /**
     * 判断当前调度线程是否已经启动
     *
     * @return true or false
     */
    public boolean isStart() {
        int startStatus = WorkerStatus.STARTED.getStatus();
        return WORKER_STATE_UPDATER.get(this) == startStatus;
    }

    /**
     * 具体启动的执行逻辑
     */
    @Override
    public void run() {
        //获取毫秒单位的当前时间戳
        startTime = System.nanoTime();
        if (startTime == 0) {
            startTime = 1;
        }
        //给时间轮设置启动时间
        timer.setStartTime(startTime);
        /**
         * 释放启动锁,通过CountDownLatch保证只有一个线程完成此操作
         * 因为时间轮的启动是第一个时间轮的任务放入的时候去启动的,所以会有并发的问题
         */
        timer.getTimerInitialLock().countDown();
        int startStatus = WorkerStatus.STARTED.getStatus();
        while (WORKER_STATE_UPDATER.get(DispatchWorker.this) == startStatus) {
            //计算并休眠到下一个tick,deadline是当前时间和startTime的便宜
            final long deadline = waitForNextTick();
            if (deadline > 0) {
                int idx = (int) (tick & timer.getBucketIndexMask());
                processCancelledTask();
                HashedWheelBucket bucket = timer.getBucketList()[idx];
                dispatchTimeoutsToBuckets();
                bucket.expireTimeouts(deadline);
                tick++;
            }
        }
        log.info("DispatchWorker is stop running success!");
        //停止扫尾工作：将不能处理的数据移动未处理的队列
        dispatchUnProcess();
        //停止扫尾工作：将取消数据的移动取消队列
        processCancelledTask();
    }

    /**
     * 功能：处理没有来得及处理的任务
     */
    private void dispatchUnProcess() {
        //移除bucket中的数据
        for (HashedWheelBucket bucket : timer.getBucketList()) {
            bucket.clearTimeouts(unprocessedTimeouts);
        }
        //移除排队队列中的数据
        while (true) {
            HashedWheelTimeout timeout = timer.getTimeouts().poll();
            if (timeout == null) {
                break;
            }
            if (!timeout.isCancelled()) {
                unprocessedTimeouts.add(timeout);
            }
        }

    }

    /**
     * 功能：讲队列中的数据移到bucket中
     */
    private void dispatchTimeoutsToBuckets() {
        for (int i = 0; i < DISPATCH_SIZE; i++) {
            HashedWheelTimeout timeout = timer.getTimeouts().poll();
            if (timeout == null) {
                break;
            }
            if (timeout.state() == HashedWheelTimeout.ST_CANCELLED) {
                continue;
            }
            long calculated = timeout.deadline / timer.getTickDuration();
            timeout.setRemainingRounds((calculated - tick) / timer.getBucketList().length);
            final long ticks = Math.max(calculated, tick);
            int stopIndex = (int) (ticks & timer.getBucketIndexMask());
            HashedWheelBucket bucket = timer.getBucketList()[stopIndex];
            bucket.addTimeout(timeout);
        }
    }

    /**
     * 功能：设置取消的任务队列
     */
    private void processCancelledTask() {
        while (true) {
            HashedWheelTimeout timeout = timer.getCancelledTimeouts().poll();
            if (timeout == null) {
                break;
            }
            try {
                timeout.remove();
            } catch (Throwable t) {
                log.warn("DispatchWorker[processCancelledTask]  is error", t);
            }
        }
    }

    /**
     * 功能：等待到下一个时间轮的触发时间
     *
     * @return
     */
    private long waitForNextTick() {
        //计算下个tick的deadline和startTime的偏移总量
        long deadline = timer.getTickDuration() * (tick + 1);
        while (true) {
            //获取当前时间和startTime的偏移
            final long currentTime = System.nanoTime() - startTime;
            //计算距离下次tick应该休眠的时间,/1000000是为了转换成毫秒,为了sleep准备(sleep只能以毫秒为单位)
            long sleepTimeMs = (deadline - currentTime + 999999) / 1000000;
            //不需要睡眠直接返回
            if (sleepTimeMs <= 0) {
                if (currentTime == Long.MIN_VALUE) {
                    return -Long.MAX_VALUE;
                } else {
                    return currentTime;
                }
            }
            // https://www.javamex.com/tutorials/threads/sleep_issues.shtml
            if (PlatformDependent.isWindows()) {
                sleepTimeMs = sleepTimeMs / 10 * 10;
            }
            try {
                //休眠到下一个tick为止
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException ignored) {
                if (WORKER_STATE_UPDATER.get(this) == WorkerStatus.SHUTDOWN.getStatus()) {
                    return Long.MIN_VALUE;
                }
            }
        }
    }

    public boolean stop() {
        int startStatus = WorkerStatus.STARTED.getStatus();
        //讲线程状态更新停止状态
        if (WORKER_STATE_UPDATER.compareAndSet(this, startStatus, WorkerStatus.SHUTDOWN.getStatus())) {
            return true;
        }
        //判断当前线程是否已经是停止状态
        else {
            int shutdown = WorkerStatus.SHUTDOWN.getStatus();
            //如果已经是停止状态直接返回true
            if (WORKER_STATE_UPDATER.getAndSet(this, shutdown) == shutdown) {
                return true;
            }
            //返回false
            else {
                return false;
            }
        }
    }

    public Set<Timeout> unprocessedTimeouts() {
        return Collections.unmodifiableSet(unprocessedTimeouts);
    }

    public int getWorkerState() {
        return workerState;
    }

    public void setWorkerState(int workerState) {
        this.workerState = workerState;
    }
}

