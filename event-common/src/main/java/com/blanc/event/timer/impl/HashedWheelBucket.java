package com.blanc.event.timer.impl;

import com.blanc.event.timer.Timeout;

import java.util.Set;

/**
 * 功能：时间轮的数据数据bucket,就是一个链表
 * @author chuchengyi
 */
public class HashedWheelBucket {

    /**
     * 功能：数据桶头指针
     */
    private HashedWheelTimeout head;

    /**
     * 功能：数据桶的尾部指针
     */
    private HashedWheelTimeout tail;

    /**
     * 功能：构造一个时间轮数据对象放到数据桶中
     * @param timeout
     */
    public void addTimeout(HashedWheelTimeout timeout) {
        assert timeout.bucket == null;
        timeout.bucket = this;
        if (head == null) {
            head = tail = timeout;
        } else {
            tail.next = timeout;
            timeout.prev = tail;
            tail = timeout;
        }
    }


    /**
     * 功能：通知数据桶需要弹出数据数据执行任务
     *
     * @param deadline
     */
    public void expireTimeouts(long deadline) {
        HashedWheelTimeout timeout = head;

        while (timeout != null) {
            HashedWheelTimeout next = timeout.next;
            if (timeout.getRemainingRounds() <= 0) {
                next = remove(timeout);
                if (timeout.deadline <= deadline) {
                    timeout.expire();
                } else {
                    throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
                }
            } else if (timeout.isCancelled()) {
                next = remove(timeout);
            } else {
                long nextRemainingRounds = timeout.getRemainingRounds() - 1;
                timeout.setRemainingRounds(nextRemainingRounds);
            }
            timeout = next;
        }
    }

    /**
     * 功能：从木桶中删除一个数据
     *
     * @param timeout
     * @return
     */
    public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
        HashedWheelTimeout next = timeout.next;
        // remove timeout that was either processed or cancelled by updating the linked-list
        if (timeout.prev != null) {
            timeout.prev.next = next;
        }
        if (timeout.next != null) {
            timeout.next.prev = timeout.prev;
        }

        if (timeout == head) {
            // if timeout is also the tail we need to adjust the entry too
            if (timeout == tail) {
                tail = null;
                head = null;
            } else {
                head = next;
            }
        } else if (timeout == tail) {
            // if the timeout is the tail modify the tail to be the prev node.
            tail = timeout.prev;
        }
        // null out prev, next and bucket to allow for GC.
        timeout.prev = null;
        timeout.next = null;
        timeout.bucket = null;
        timeout.timer.pendingTimeouts.decrementAndGet();
        return next;
    }

    /**
     * 功能 清除一个木桶信息
     *
     * @param set
     */
    public void clearTimeouts(Set<Timeout> set) {
        while (true) {
            HashedWheelTimeout timeout = pollTimeout();
            if (timeout == null) {
                return;
            }
            if (timeout.isExpired() || timeout.isCancelled()) {
                continue;
            }
            set.add(timeout);
        }
    }

    private HashedWheelTimeout pollTimeout() {
        HashedWheelTimeout head = this.head;
        if (head == null) {
            return null;
        }
        HashedWheelTimeout next = head.next;
        if (next == null) {
            tail = this.head = null;
        } else {
            this.head = next;
            next.prev = null;
        }

        head.next = null;
        head.prev = null;
        head.bucket = null;
        return head;
    }
}