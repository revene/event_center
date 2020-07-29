/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blanc.event.sharding;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collection;

/**
 * sharding 数据源hint算法
 * tableindex:路由索引 % 分库数量
 *
 * @author wangbaoliang
 */
public final class DataBaseHintShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    /**
     * sharding分库的数量
     */
    public static final Integer DATABASE_SIZE = 2;

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final HintShardingValue<Integer> shardingValue) {
        Collection<String> result = new ArrayList<>();
        for (String each : availableTargetNames) {
            for (Integer value : shardingValue.getValues()) {
                if (each.endsWith(String.valueOf(value % DATABASE_SIZE))) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
