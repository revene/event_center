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
 * 功能：时间轮接口信息
 *
 * @author chuchengyi
 */
public interface Timer {

    /**
     * 功能：将数据加入到时间轮
     *
     * @param task
     * @param delay
     * @param unit
     * @return
     */
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    /**
     * 功能：停止时间轮
     *
     * @return
     */
    Set<Timeout> stop();
}
