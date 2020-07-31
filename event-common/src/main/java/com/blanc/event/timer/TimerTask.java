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
package com.blanc.event.timer;


/**
 * 时间轮: TimerTask任务的包装类
 *
 * @param <T>
 */
public interface TimerTask<T> {

    /**
     * 功能：执行某个事件
     *
     * @param timeout 超时时间
     * @throws Exception 异常
     */
    void run(Timeout timeout) throws Exception;


    /**
     * 功能：返回具体的数据
     *
     * @return 获取要执行的事件
     */
    T getTask();

    /**
     * 获取任务的执行器
     *
     * @return 任务执行器
     */
    TaskExecutor getTaskExecutor();


    /**
     * 功能：获取当前任务的id
     *
     * @return id
     */
    String getId();


}
