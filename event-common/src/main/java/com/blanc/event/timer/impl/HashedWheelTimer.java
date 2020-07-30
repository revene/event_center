/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.blanc.event.timer.impl;

import com.blanc.event.timer.Timeout;
import com.blanc.event.timer.Timer;
import com.blanc.event.timer.TimerTask;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 时间轮
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "timer")
public class HashedWheelTimer implements Timer {


    /**
     * 功能：全局可以定义最大实例的个数
     */
    private static final int INSTANCE_COUNT_LIMIT = 64;

    /**
     * 功能：具体实例个数的计数器
     */
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();

    /**
     * 功能：进行调度线程
     */
    private final Thread workerThread;

    /**
     * 功能：调度器
     */
    private final DispatchWorker worker = new DispatchWorker(this);

    /**
     * 时间轮的基本时间跨度:描述了时间轮时间控制的精度
     */
    private final long tickDuration;

    /**
     * 功能：bucketList索引边界
     */
    private final int bucketIndexMask;

    /**
     * 时间轮的桶:即定时任务的列表
     */
    private final HashedWheelBucket[] bucketList;

    /**
     * 功能：时间轮的启动锁
     */
    private final CountDownLatch timerInitialLock = new CountDownLatch(1);

    /**
     * 功能：定义时间轮的数据索引
     */
    private final ConcurrentHashMap<String, String> dataIndexMap = new ConcurrentHashMap<>();

    /**
     * 等待放入到时间轮中的HashedWheelTimerout队列缓存
     */
    private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();

    /**
     * 功能：取消的时间轮数据
     */
    private final Queue<HashedWheelTimeout> cancelledTimeouts = PlatformDependent.newMpscQueue();

    /**
     * 功能：在队列中等待的数据格式
     */
    public final AtomicLong pendingTimeouts = new AtomicLong(0);

    /**
     * 功能：队列最大等待格式
     */
    private final long maxPendingTimeouts;

    /**
     * 时间轮的启动时间
     */
    private volatile long startTime;

    /**
     * 默认的bucket的个数 即一个时间轮有多少个格子，这里默认是512格，需要是2的n次方，用于确保hash运算的方便
     */
    private static final int DEFAULT_BUCKET_SIZE = 512;

    /**
     * 功能：最大的bucket的个数
     */
    private static final int MAX_BUCKET_SIZE = 1073741824;

    /**
     * 功能：1ns时间常量的定义
     */
    private static final long MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    /**
     * 功能：使用默认的线程池和时间片来构造一个时间轮 默认的ThreadFactory
     */
    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    /**
     * 使用默认的线程池和制定的事件间隔来构造一个时间轮
     *
     * @param tickDuration
     * @param unit
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    /**
     * 使用默认的线程池和制定的事件间隔来构造一个时间轮
     *
     * @param tickDuration  时间轮的间隔
     * @param unit          时间轮的时间单位
     * @param ticksPerWheel 时间片的个数
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }


    /**
     * 功能：执行线程池 按照100ms来构造时间轮
     *
     * @param threadFactory
     */
    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100, TimeUnit.MILLISECONDS);
    }


    /**
     * 功能：根据线程池和时间间隔来创建时间轮
     *
     * @param threadFactory
     * @param tickDuration
     * @param unit
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, DEFAULT_BUCKET_SIZE);
    }


    /***
     * 功能：通过线程工程和时间间隔已经时间轮的格数来创建时间轮
     * @param threadFactory
     * @param tickDuration
     * @param unit
     * @param ticksPerWheel
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, -1);
    }


    /**
     * 功能：检验构造方法的入参情况
     *
     * @param threadFactory
     * @param tickDuration
     * @param unit
     * @param ticksPerWheel
     */
    private void checkPram(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (tickDuration <= 0) {
            throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
        }
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
    }

    /**
     * 功能：最终构造方法
     *
     * @param threadFactory
     * @param tickDuration
     * @param unit
     * @param ticksPerWheel
     * @param maxPendingTimeouts The maximum number of pending timeouts after which call to
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, long maxPendingTimeouts) {
        //检验参数
        checkPram(threadFactory, tickDuration, unit, ticksPerWheel);
        bucketList = createWheel(ticksPerWheel);
        bucketIndexMask = bucketList.length - 1;

        // 讲间隔时间转换成NS
        long duration = unit.toNanos(tickDuration);

        // Prevent overflow.
        long maxDuration = Long.MAX_VALUE / bucketList.length;
        if (duration >= maxDuration) {
            String error = String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", tickDuration, maxDuration);
            throw new IllegalArgumentException();
        }
        if (duration < MILLISECOND_NANOS) {
            this.tickDuration = MILLISECOND_NANOS;
        } else {
            this.tickDuration = duration;
        }

        //构造监控线程
        workerThread = threadFactory.newThread(worker);

        this.maxPendingTimeouts = maxPendingTimeouts;

        if (INSTANCE_COUNTER.incrementAndGet() > INSTANCE_COUNT_LIMIT) {
            reportTooManyInstances();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {

        }
    }

    /**
     * 功能：创建时间轮数字
     *
     * @param bucketSize
     * @return
     */
    private HashedWheelBucket[] createWheel(int bucketSize) {
        if (bucketSize > MAX_BUCKET_SIZE) {
            throw new IllegalArgumentException("ticksPerWheel can not greater than 2^30: " + bucketSize);
        }
        bucketSize = PlatformDependent.normalizeTicksPerWheel(bucketSize);
        HashedWheelBucket[] wheel = new HashedWheelBucket[bucketSize];
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }


    /**
     * 功能：启动线程
     */
    public void start() {
        //当没有启动尝试启动
        if (!this.worker.isStart()) {
            synchronized (this) {
                this.worker.start();
                workerThread.start();
            }
        }
        /**
         * MESA模型的写法,如果发现条件不满足,则await直到别的线程asign唤醒后,继续执行,所以只要startTime为0,就阻塞当前线程
         * 直到startTime被赋值
         */
        while (startTime == 0) {
            try {
                timerInitialLock.await();
            } catch (InterruptedException ignore) {
                log.error("HashWheelTimer [timerInitialLock.await()] is error, caused by {}",
                        Throwables.getStackTraceAsString(ignore));
            }
        }
    }


    @Override
    public Set<Timeout> stop() {
        //当前线程不是工作线程不能停止 确保线程安全性
        if (Thread.currentThread() == workerThread) {
            throw new IllegalStateException(".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }

        if (!this.worker.stop()) {
            INSTANCE_COUNTER.decrementAndGet();
        } else {
            return worker.unprocessedTimeouts();
        }
        try {
            boolean interrupted = false;
            while (workerThread.isAlive()) {
                workerThread.interrupt();
                try {
                    workerThread.join(100);
                } catch (InterruptedException ignored) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        } finally {
            INSTANCE_COUNTER.decrementAndGet();
        }
        return worker.unprocessedTimeouts();
    }


    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        //当缓存中不包含任务的时间才添加 Key是event的主键id
        if (!this.getDataIndexMap().containsKey(task.getId())) {
            //计数器，没在时间轮中添加一个事件就将count+1;
            long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();
            //todo 这个pendingTimeOuts是个啥意思?
            if (maxPendingTimeouts > 0 && pendingTimeoutsCount > maxPendingTimeouts) {
                pendingTimeouts.decrementAndGet();
                throw new RejectedExecutionException("Number of pending timeouts ("
                        + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending "
                        + "timeouts (" + maxPendingTimeouts + ")");
            }
            //时间轮在构造的时候并没有启动,而这里是启动了时间轮
            start();
            //计算该任务的相对时间轮启动时间的偏移时间,单位是纳秒
            long deadline = System.nanoTime() + unit.toNanos(delay) - startTime;
            //todo 这里是什么意思?
            if (delay > 0 && deadline < 0) {
                deadline = Long.MAX_VALUE;
            }
            //将TimerTask包装成HashedWheelTimerout(时间轮的定时任务基本单位)
            HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
            //添加到timeouts的缓存队列中,随后再真的放入到时间轮中去执行
            timeouts.add(timeout);
            this.dataIndexMap.put(task.getId(), task.getId());
            return timeout;
        }
        return null;
    }

    /**
     * 功能：获取到等待队列
     *
     * @return
     */
    public long pendingTimeouts() {
        return pendingTimeouts.get();
    }

    private static void reportTooManyInstances() {
        String resourceType = PlatformDependent.simpleClassName(HashedWheelTimer.class);
        log.error("You are creating too many " + resourceType + " instances. " +
                resourceType + " is a shared resource that must be reused across the JVM," +
                "so that only a few instances are created.");

    }


    public long getTickDuration() {
        return tickDuration;
    }

    public int getBucketIndexMask() {
        return bucketIndexMask;
    }


    public CountDownLatch getTimerInitialLock() {
        return timerInitialLock;
    }

    public Queue<HashedWheelTimeout> getTimeouts() {
        return timeouts;
    }

    public Queue<HashedWheelTimeout> getCancelledTimeouts() {
        return cancelledTimeouts;
    }

    public AtomicLong getPendingTimeouts() {
        return pendingTimeouts;
    }

    public long getMaxPendingTimeouts() {
        return maxPendingTimeouts;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public HashedWheelBucket[] getBucketList() {
        return bucketList;
    }

    public ConcurrentHashMap<String, String> getDataIndexMap() {
        return dataIndexMap;
    }
}
