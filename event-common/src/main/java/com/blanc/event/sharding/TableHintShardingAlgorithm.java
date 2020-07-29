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

import java.util.ArrayList;
import java.util.Collection;

/**
 * table:event 的hint分表算法
 *
 * @author wangbaoliang
 */
public final class TableHintShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    /**
     * 数据库里nodeScan配置的扫描节点的总数量
     */
    private static final Integer NODE_COUNT = 4;

    /**
     * 平均每个数据库落下的扫描节点的数量
     */
    private static final Integer NODE_PER_DATABASE = DataBaseHintShardingAlgorithm.DATABASE_SIZE / NODE_COUNT;

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final HintShardingValue<Integer> shardingValue) {
        Collection<String> result = new ArrayList<>();
        for (String each : availableTargetNames) {
            //取出物理表的尾号
            final Integer tableIndex = Integer.valueOf(each.split("_")[1]);
            for (Integer value : shardingValue.getValues()) {
                if (tableIndex % DataBaseHintShardingAlgorithm.DATABASE_SIZE == value / DataBaseHintShardingAlgorithm.DATABASE_SIZE){
                    result.add(each);
                }
            }
        }
        System.out.println(result);
        return result;
    }
}
