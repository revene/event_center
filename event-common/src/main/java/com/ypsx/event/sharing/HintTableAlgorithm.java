package com.ypsx.event.sharing;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 功能：HINT分表的策略
 *
 * @author chuchengyi
 */
@Component("hintTableAlgorithm")
public class HintTableAlgorithm extends AbstractHintAlgorithm implements HintShardingAlgorithm {

    @Resource
    private HintTableExpression hintTableExpression;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        //转成Sting类型的分片策略
        ListShardingValue<String> tmpShared = (ListShardingValue<String>) shardingValue;
        //转成标准的分片策略
        PreciseShardingValue<String> tmpValue = this.convert(tmpShared);
        //判断下是否是直接指定物理表的场景
        if (hintTableExpression.matchExpression(tmpValue.getValue())) {
            return doHintShare(availableTargetNames, tmpValue);
        } else {
            return doNomalShare(availableTargetNames, tmpValue);
        }

    }


    /**
     * 功能：正常的表路由
     *
     * @param collection
     * @param shardingValue
     * @return
     */
    public List<String> doNomalShare(Collection<String> collection, PreciseShardingValue<String> shardingValue) {

        List<String> result = new ArrayList<>();
        Long hashCode = Long.valueOf(shardingValue.getValue().hashCode());
        if (hashCode <= 0) {
            hashCode = 0 - hashCode;
        }
        Long mod = hashCode % collection.size();
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
    public List<String> doHintShare(Collection<String> collection, PreciseShardingValue<String> value) {
        List<String> result = new ArrayList<>();
        //获取到具体的index
        int mod = hintTableExpression.getTableIndex(value.getValue());
        mod = mod % collection.size();
        for (String each : collection) {
            if (each.endsWith(mod + "")) {
                result.add(each);
            }
        }
        return result;
    }
}
