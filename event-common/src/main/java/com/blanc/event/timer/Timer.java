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

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮提供的方法接口
 *
 * @author wangbaoliang
 */
public interface Timer {

    /**
     * 将一个timerTask定时任务加入到时间轮中,如果是第一个任务,则启动时间轮
     *
     * @param task  封装的定时任务
     * @param delay 延迟执行时间
     *              比如希望在时间戳100执行,但是现在的时间戳是70,则delay是30
     *              如果希望在时间戳100执行,但是现在的时间戳是120,则delay上游给的是默认的300ms
     * @param unit  延迟执行事件单位
     * @return 时间轮执行单位
     */
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    /**
     * 停止时间轮
     *
     * @return 还未处理的Timerout任务
     */
    Set<Timeout> stop();
}
