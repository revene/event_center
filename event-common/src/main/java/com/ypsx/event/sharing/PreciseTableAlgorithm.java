package com.ypsx.event.sharing;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 功能：数据库的分表规则
 *
 * @author chuchengyi
 */
@Component
public class PreciseTableAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> value) {
        return doNormalShard(tableNames, value);
    }


    /**
     * 功能：正常的数据路由信息
     *
     * @param tableNames
     * @param value
     * @return
     */
    public String doNormalShard(Collection<String> tableNames, PreciseShardingValue<String> value) {
        //获取hash值
        Integer hashCode = Integer.valueOf(value.getValue().hashCode());
        if (hashCode <= 0) {
            hashCode = 0 - hashCode;
        }
        for (String each : tableNames) {
            Integer mod = hashCode % tableNames.size();
            if (each.endsWith(mod + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

}
