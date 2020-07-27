package com.blanc.event.timer.impl;

import com.blanc.event.timer.Timeout;
import com.blanc.event.timer.Timer;
import com.blanc.event.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 功能：整个时间轮
 *
 * @author chuchengyi
 */
public class HashedWheelTimeout implements Timeout {


    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("timerLog");

    /**
     * 功能：初始化状态
     */
    public static final int ST_INIT = 0;
    /**
     * 功能：取消状态
     */
    public static final int ST_CANCELLED = 1;
    /**
     * 功能：是否超时
     */
    public static final int ST_EXPIRED = 2;


    private static final String STATE_FER = "state";
    /**
     * 功能：使用CAS原理的乐观锁来更新状态字段
     */
    private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, STATE_FER);

    public final HashedWheelTimer timer;
    /**
     * 功能：任务信息
     */
    private final TimerTask task;

    /**
     * 功能：到期时间
     */
    public final long deadline;

    @SuppressWarnings({"unused", "FieldMayBeFinal", "RedundantFieldInitialization"})
    private volatile int state = ST_INIT;


    private long remainingRounds;

    /**
     * 功能：时间数据的后置指针
     */
    public HashedWheelTimeout next;

    /**
     * 功能：时间数据的前置指针
     */
    public HashedWheelTimeout prev;


    /**
     * 功能更：指向的数据数据桶
     */
    HashedWheelBucket bucket;

    HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
        this.timer = timer;
        this.task = task;
        this.deadline = deadline;
    }

    @Override
    public Timer timer() {
        return timer;
    }

    @Override
    public TimerTask task() {
        return task;
    }

    @Override
    public boolean cancel() {
        // only update the state it will be removed from HashedWheelBucket on next tick.
        if (!compareAndSetState(ST_INIT, ST_CANCELLED)) {
            return false;
        }
        timer.getCancelledTimeouts().add(this);
        return true;
    }

    void remove() {
        HashedWheelBucket bucket = this.bucket;
        if (bucket != null) {
            //删除索引信息
            this.timer.getDataIndexMap().remove(task.getId());
            bucket.remove(this);
        } else {
            timer.pendingTimeouts.decrementAndGet();
        }
    }

    public boolean compareAndSetState(int expected, int state) {
        return STATE_UPDATER.compareAndSet(this, expected, state);
    }

    public int state() {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return state() == ST_CANCELLED;
    }

    @Override
    public boolean isExpired() {
        return state() == ST_EXPIRED;
    }

    public void expire() {
        if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
            return;
        }
        try {
            //功能删除索引信息
            this.timer.getDataIndexMap().remove(task.getId());
            task.run(this);
        } catch (Throwable t) {
            logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);

        }
    }

    @Override
    public String toString() {
        final long currentTime = System.nanoTime();
        long remaining = deadline - currentTime + timer.getStartTime();

        StringBuilder buf = new StringBuilder(192)
                .append(PlatformDependent.simpleClassName(this.getClass()))
                .append('(')
                .append("deadline: ");
        if (remaining > 0) {
            buf.append(remaining)
                    .append(" ns later");
        } else if (remaining < 0) {
            buf.append(-remaining)
                    .append(" ns ago");
        } else {
            buf.append("now");
        }

        if (isCancelled()) {
            buf.append(", cancelled");
        }

        return buf.append(", task: ")
                .append(task())
                .append(')')
                .toString();
    }

    public long getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(long remainingRounds) {
        this.remainingRounds = remainingRounds;
    }


    public TimerTask getTask() {
        return task;
    }

    public long getDeadline() {
        return deadline;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashedWheelTimeout that = (HashedWheelTimeout) o;
        return deadline == that.deadline &&
                state == that.state &&
                remainingRounds == that.remainingRounds &&
                Objects.equals(timer, that.timer) &&
                Objects.equals(task, that.task) &&
                Objects.equals(next, that.next) &&
                Objects.equals(prev, that.prev) &&
                Objects.equals(bucket, that.bucket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timer, task, deadline, state, remainingRounds, next, prev, bucket);
    }
}
