package com.ypsx.event.sharing;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author chuchengyi
 */
public abstract class AbstractHintAlgorithm {

    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    /**
     * 功能：将hint转成标准分片的策略
     *
     * @param shareValue
     * @return
     */
    public PreciseShardingValue<String> convert(ListShardingValue<String> shareValue) {
        //获取列明
        String columnName = shareValue.getColumnName();
        //获取到具体的数据值
        Collection<String> valueList = shareValue.getValues();

        if (valueList.isEmpty()) {
            logger.error("AbstractHintAlgorithm[convert]  valueList is null" + valueList);
        }
        //获取到逻辑表名称
        String logicTableName = shareValue.getLogicTableName();
        //对类型进行转换
        String shardingValue = null;
        for (String value : valueList) {
            if (value != null && !value.isEmpty()) {
                shardingValue = value;
                break;
            }
        }
        PreciseShardingValue<String> result = new PreciseShardingValue<String>(logicTableName, columnName, shardingValue);
        return result;
    }
}
