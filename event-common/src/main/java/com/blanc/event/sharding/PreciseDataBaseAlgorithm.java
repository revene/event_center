//package com.ypsx.event.sharing;
//
//import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
//import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
///**
// * 功能：数据库的分库
// *
// * @author chuchengyi
// */
//@Component
//public class PreciseDataBaseAlgorithm implements PreciseShardingAlgorithm<String> {
//
//    @Override
//    public String doSharding(Collection<String> collection, PreciseShardingValue<String> value) {
//        return doNormalShard(collection, value);
//
//    }
//
//
//    /**
//     * 功能：正常的表路由
//     *
//     * @param collection
//     * @param value
//     * @return
//     */
//    public String doNormalShard(Collection<String> collection, PreciseShardingValue<String> value) {
//        Long hashCode = Long.valueOf(value.getValue().hashCode());
//        if (hashCode <= 0) {
//            hashCode = 0 - hashCode;
//        }
//        for (String each : collection) {
//
//            Long mod = hashCode % collection.size();
//            if (each.endsWith(mod + "")) {
//                return each;
//            }
//        }
//        throw new UnsupportedOperationException();
//    }
//
//
//}