//package com.ypsx.event.manager.impl;
//
//import com.ypsx.event.sharing.HintTableExpression;
//import io.shardingsphere.api.HintManager;
//
//import javax.annotation.Resource;
//
///**
// * hint强制路由算法
// *
// * @author wangbaoliang
// */
//public abstract class AbstractHintManager {
//
//    /**
//     * 强制路由的事件表 event
//     */
//    public static final String EVENT_TABLE = "event";
//
//    /**
//     * 强制路由的事件执行日志表 event_log
//     */
//    public static final String LOG_TABLE = "event_log";
//
//    /**
//     * 强制路由算法
//     */
//    @Resource
//    private HintTableExpression hintTableExpression;
//
//    /**
//     * 功能：初始化hint路由算法(包含库路由和表路由)
//     *
//     * @param table         要强制路由的表
//     * @param databaseIndex 库路由索引
//     * @param tableIndex    表路由索引
//     * @return hintManager实例
//     */
//    public HintManager initHint(String table, String databaseIndex, String tableIndex) {
//        HintManager hintManager = HintManager.getInstance();
//        hintManager.addDatabaseShardingValue(table, databaseIndex);
//        hintManager.addTableShardingValue(table, tableIndex);
//        return hintManager;
//    }
//
//
//    /**
//     * 功能：初始化HINT的路由算法(只包含表路由,即没有分库)
//     *
//     * @param table      要强制路由的表
//     * @param tableIndex 表路由索引
//     * @return hintManager实例
//     */
//    public HintManager initHint(String table, int tableIndex) {
//        HintManager hintManager = HintManager.getInstance();
//        String shareIndex = hintTableExpression.genExpression(tableIndex);
//        hintManager.addDatabaseShardingValue(table, shareIndex);
//        hintManager.addTableShardingValue(table, shareIndex);
//        return hintManager;
//
//    }
//
//    /**
//     * 获取强制路由算法
//     *
//     * @return 当前的强制路由算法
//     */
//    public HintTableExpression getHintTableExpression() {
//        return hintTableExpression;
//    }
//
//    /**
//     * 设置强制路由算法
//     *
//     * @param hintTableExpression 强制路由算法
//     */
//    public void setHintTableExpression(HintTableExpression hintTableExpression) {
//        this.hintTableExpression = hintTableExpression;
//    }
//}
