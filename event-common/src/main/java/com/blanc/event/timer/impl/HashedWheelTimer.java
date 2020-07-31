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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
@Data
@Slf4j(topic = "timer")
public class HashedWheelTimer implements Timer {


    /**
     * 全局可以定义最大时间轮的个数
     */
    private static final int INSTANCE_COUNT_LIMIT = 64;

    /**
     * 当前时间轮的个数计数器
     */
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();

    /**
     * 功能：进行调度线程
     */
    private final Thread workerThread;

    /**
     * 功能：调度器
     */
    private final DispatchWorker dispatchWorker = new DispatchWorker(this);

    /**
     * 时间轮的基本时间跨度:描述了时间轮时间控制的精度
     */
    private final long tickDuration;

    /**
     * bucketList的最大索引,比如bucketList的length是10,则bucketIndexMask是9
     */
    private final int bucketIndexMask;

    /**
     * 时间轮的桶:即定时任务的列表
     */
    private final HashedWheelBucket[] bucketList;

    /**
     * 时间轮的启动锁,本质上是countDownLatch
     */
    private final CountDownLatch timerInitialLock = new CountDownLatch(1);

    /**
     * 功能：定义时间轮的数据索引
     */
    private final ConcurrentHashMap<String, String> dataIndexMap = new ConcurrentHashMap<>();

    /**
     * 等待放入到时间轮中的HashedWheelTimerout队列缓存
     */
    private final Queue<HashedWheelTimeout> timeoutsCache = PlatformDependent.newMpscQueue();

    /**
     * 功能：取消的时间轮数据
     */
    private final Queue<HashedWheelTimeout> cancelledTimeouts = PlatformDependent.newMpscQueue();

    /**
     * 功能：在队列中等待的数据格式
     */
    public final AtomicLong pendingTimeouts = new AtomicLong(0);

    /**
     * timeout队列缓存的最大容纳HashedWheelTimerout的数量
     */
    private final long maxPendingTimeouts;

    /**
     * 时间轮的启动时间
     */
    private volatile long startTime;

    /**
     * 默认的bucket的个数 即一个时间轮有多少个格子，这里默认是512格，需要是2的n次方，用于确保>>运算的方便
     */
    private static final int DEFAULT_BUCKET_SIZE = 512;

    /**
     * 功能：最大的bucket的个数
     */
    private static final int MAX_BUCKET_SIZE = 1073741824;

    /**
     * 1毫秒ms对应的纳秒ns数
     */
    private static final long MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    /**
     * 功能：使用默认的线程池和时间片来构造一个时间轮 默认的ThreadFactory
     */
    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }


    /**
     * 功能：执行线程池 按照100ms一个格子来构造时间轮
     *
     * @param threadFactory 线程工厂
     */
    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100, TimeUnit.MILLISECONDS);
    }


    /**
     * 功能：根据线程池和时间间隔来创建时间轮
     *
     * @param threadFactory 线程工厂
     * @param tickDuration  时间轮时间精度
     * @param unit          时间轮时间精度单位
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, DEFAULT_BUCKET_SIZE);
    }


    /***
     * 功能：通过线程工程和时间间隔已经时间轮的格数来创建时间轮
     *
     * @param threadFactory 线程工厂
     * @param tickDuration  时间轮时间精度
     * @param unit          时间轮时间精度单位
     * @param ticksPerWheel 时间轮期望的格子数(实际取最小的大于ticksPerWheel的2^n数)
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, -1);
    }


    /**
     * 功能：检验构造方法的入参情况
     *
     * @param threadFactory 构造线程的线程工厂类
     * @param tickDuration  时间轮的时间精度:每一格的时间
     * @param unit          时间轮的时间精度单位
     * @param ticksPerWheel 期望的时间轮格数
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
     * @param threadFactory      构造线程的线程工厂
     * @param tickDuration       时间轮每一格的时间,即控制的时间单位精度
     * @param unit               每一格时间的精度
     * @param ticksPerWheel      期望的时间轮格子数
     * @param maxPendingTimeouts The maximum number of pending timeouts after which call to
     */
    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, long maxPendingTimeouts) {
        //检验参数
        checkPram(threadFactory, tickDuration, unit, ticksPerWheel);
        //创建ticksPerWheel个桶数组,实际上是比ticksPerWheel大的最小的2^n个桶数组,因为计算任务应该在哪个格子的时候,2^n可以通过>>达到%的效果,且效果更好
        bucketList = createWheel(ticksPerWheel);
        bucketIndexMask = bucketList.length - 1;

        //将时间格的时间转换成纳秒
        long duration = unit.toNanos(tickDuration);

        //计算Long值情况下,允许的最大的时间格精度时间,如果超过则报错
        long maxDuration = Long.MAX_VALUE / bucketList.length;
        if (duration >= maxDuration) {
            String error = String.format("tickDuration : %d is out of range, (expected: 0 < tickDuration in nanos < %d", tickDuration, maxDuration);
            throw new IllegalArgumentException(error);
        }
        // 不能小于最小精度1ms,不然设置成1ms,这个地方应该没啥用,因为传入的tickDuration本身就是MILLISECOND_NANOS的整数倍
        if (duration < MILLISECOND_NANOS) {
            this.tickDuration = MILLISECOND_NANOS;
        } else {
            this.tickDuration = duration;
        }

        //构造监控线程
        workerThread = threadFactory.newThread(dispatchWorker);

        this.maxPendingTimeouts = maxPendingTimeouts;

        //时间轮不能超过最大的实例个数,64个
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
     * 创建时间轮格子数对应的HashedWheelBucket数组
     *
     * @param bucketSize 希望的时间隔格数
     * @return HashedWheelBucket数组
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
        //尝试启动调度线程
        if (!this.dispatchWorker.isStart()) {
            synchronized (this) {
                this.dispatchWorker.start();
                workerThread.start();
            }
        }
        while (startTime == 0) {
            try {
                timerInitialLock.await();
            } catch (InterruptedException ignore) {
                log.error("HashWheelTimer [timerInitialLock.await()] is error, caused by {}",
                        Throwables.getStackTraceAsString(ignore));
            }
        }
    }

    /**
     * 停止时间轮
     *
     * @return 还未处理的Timerout任务
     */
    @Override
    public Set<Timeout> stop() {
        //当前线程不是工作线程不能停止 确保线程安全性
        if (Thread.currentThread() == workerThread) {
            throw new IllegalStateException(".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }

        if (!this.dispatchWorker.stop()) {
            INSTANCE_COUNTER.decrementAndGet();
        } else {
            return dispatchWorker.unprocessedTimeouts();
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
        return dispatchWorker.unprocessedTimeouts();
    }

    /**
     * 将一个timerTask定时任务加入到时间轮中,如果是第一个任务,则启动时间轮
     *
     * @param task  封装的定时任务
     * @param delay 延迟执行时间
     * @param unit  延迟执行事件单位
     * @return 时间轮执行单位
     */
    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        //当缓存中不包含任务的时间才添加 Key是event的主键id
        if (!this.getDataIndexMap().containsKey(task.getId())) {
            //timerout缓存队列计数器 + 1, 不能 > 限定的最大的数量,超过则抛出reject异常
            long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();
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
            //delay上游一定是 >= 0的, deadline不可能小于0吧,因为java是有符号数,所以如果偏移量过大的情况下,是会变成负数的,所以这里做个校验
            if (delay > 0 && deadline < 0) {
                deadline = Long.MAX_VALUE;
            }
            //将TimerTask包装成HashedWheelTimerout(时间轮的定时任务基本单位)
            HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
            //添加到timeouts的缓存队列中,随后再真的放入到时间轮中去执行
            timeoutsCache.add(timeout);
            //放入到业务event的缓存中
            this.dataIndexMap.put(task.getId(), task.getId());
            return timeout;
        }
        return null;
    }

    /**
     * 日志记录创建了超过允许个数的时间轮
     */
    private static void reportTooManyInstances() {
        String resourceType = PlatformDependent.simpleClassName(HashedWheelTimer.class);
        log.error("You are creating too many " + resourceType + " instances. " +
                resourceType + " is a shared resource that must be reused across the JVM," +
                "so that only a few instances are created.");

    }

}
