package com.liuyiling.coding.mysql.table;

import com.liuyiling.coding.mysql.jdbc.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/29.
 */
public class TableContainer {

    public Map<String, TableItem> tableItems;
    private static Map<String, TableChannel> channelMap = new ConcurrentHashMap<>();
    private static AtomicLong atomicLong = new AtomicLong(15);

    public TableItem getTableItem(String tableItemName){
        if(CollectionUtils.isEmpty(tableItems)){
            return tableItems.get(tableItemName);
        }
        return null;
    }

    public TableChannel getDefaultTableChannel(String tableItemName, String sqlName) {
        TableItem tableItem = this.getTableItem(tableItemName);


        String tableFullName = tableItem.getTableNamePrefix();
        String dbFullName = tableItem.getDbNamePrefix();
        String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);

        TableChannel channel = channelMap.get(sqlKey);
        if (channel == null) {
            channel = new TableChannel();
            JdbcTemplate template = tableItem.getJdbcTemplates()[0];
            String sql = TableItemHelper.getSql(tableItem, sqlName);
            channel.setSql(sql);
            channel.setJdbcTemplate(template);
            channel.setId(atomicLong.incrementAndGet() + System.currentTimeMillis());
            channel.setJdbcTemplate(template);
            channelMap.put(sqlKey, channel);
        }
        return channel;
    }

    public TableChannel getTableChannel(String tableItemName, String sqlName, Long dbStrategyId, Date date) {
        TableItem tableItem = this.getTableItem(tableItemName);
        String tableFullName = TableItemHelper.getTableName(tableItem, date);
        String dbFullName = TableItemHelper.getDbName(tableItem, dbStrategyId);
        String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);
        TableChannel channel = (TableChannel)channelMap.get(sqlKey);
        if(channel == null) {
            channel = new TableChannel();
            JdbcTemplate template = TableItemHelper.getJdbcTemplate(tableItem, dbStrategyId);
            String sql = TableItemHelper.getSql(tableItem, sqlName, dbStrategyId, date);
            channel.setSql(sql);
            channel.setJdbcTemplate(template);
            channel.setId(Long.valueOf(atomicLong.incrementAndGet() + System.currentTimeMillis()));
            channel.setJdbcTemplate(template);
            channelMap.put(sqlKey, channel);
        }

        return channel;
    }

    public TableChannel getTableChannel(String tableItemName, String sqlName, Long dbStrategyId, Long tableStrategyId){
        TableItem tableItem = getTableItem(tableItemName);

        String tableFullName = TableItemHelper.getTableName(tableItem, tableStrategyId);
        String dbFullName = TableItemHelper.getDbName(tableItem, dbStrategyId);
        String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);

        TableChannel channel = channelMap.get(sqlKey);
        if(channel == null){
            channel = new TableChannel();
            JdbcTemplate template = TableItemHelper.getJdbcTemplate(tableItem, dbStrategyId);
            String sql = TableItemHelper.getSql(tableItem, sqlName, dbStrategyId, tableStrategyId);
            channel.setSql(sql);
            channel.setJdbcTemplate(template);
            channel.setId(atomicLong.incrementAndGet() + System.currentTimeMillis());
            channel.setJdbcTemplate(template);
            channelMap.put(sqlKey, channel);
        }
        return channel;
    }



}
