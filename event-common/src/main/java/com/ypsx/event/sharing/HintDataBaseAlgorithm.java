package com.ypsx.event.sharing;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chuchengyi
 */
@Component("hintDataBaseAlgorithm")
public class HintDataBaseAlgorithm extends AbstractHintAlgorithm implements HintShardingAlgorithm {

    @Resource
    private HintTableExpression hintTableExpression;


    @Value("${database.split.table.size}")
    private int tableSize;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {

        ListShardingValue<String> tmpShard = (ListShardingValue<String>) shardingValue;

        PreciseShardingValue<String> tmpValue = this.convert(tmpShard);
        if (hintTableExpression.matchExpression(tmpValue.getValue())) {
            return doHintShard(availableTargetNames, tmpValue);

        } else {
            return doNomalShard(availableTargetNames, tmpValue);
        }

    }


    /**
     * 功能：正常的表路由
     *
     * @param collection
     * @param shardingValue
     * @return
     */
    public List<String> doNomalShard(Collection<String> collection, PreciseShardingValue<String> shardingValue) {

        List<String> result = new ArrayList<>();
        Long hashCode = Long.valueOf(shardingValue.getValue().hashCode());
        if (hashCode <= 0) {
            hashCode = 0 - hashCode;
        }
        int allTableSize = tableSize * collection.size();
        Long mod = (hashCode % allTableSize) / tableSize;
        for (String each : collection) {
            if (each.endsWith(mod + "")) {
                result.add(each);
            }
        }
        return result;
    }


    /**
     * 功能：具体到具体物理表的hint路由
     *
     * @param collection
     * @param value
     * @return
     */
    public List<String> doHintShard(Collection<String> collection, PreciseShardingValue<String> value) {
        List<String> result = new ArrayList<>();
        int mod = hintTableExpression.getTableIndex(value.getValue());
        mod = mod / tableSize;
        for (String each : collection) {
            if (each.endsWith(mod + "")) {
                result.add(each);
            }
        }
        return result;
    }


}
