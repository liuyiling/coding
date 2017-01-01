package com.liuyiling.coding.mysql.table;

import com.liuyiling.coding.mysql.jdbc.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/29.
 */
public class TableContainer {

    public Map<String, TableItem> tableItems;
    private static Map<String, TableChannel> channelMap = new ConcurrentHashMap<>();
    private static AtomicLong atomicLong = new AtomicLong(15);

    public TableItem getTableItem(String tableItemName) {
        if (CollectionUtils.isEmpty(tableItems)) {
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
        TableChannel channel = (TableChannel) channelMap.get(sqlKey);
        if (channel == null) {
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

    public TableChannel getTableChannel(String tableItemName, String sqlName, Long dbStrategyId, Long tableStrategyId) {
        TableItem tableItem = getTableItem(tableItemName);

        String tableFullName = TableItemHelper.getTableName(tableItem, tableStrategyId);
        String dbFullName = TableItemHelper.getDbName(tableItem, dbStrategyId);
        String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);

        TableChannel channel = channelMap.get(sqlKey);
        if (channel == null) {
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

    public List<TableChannel> getAllTableChannel(String tableItemName, String sqlName) {
        TableItem tableItem = this.getTableItem(tableItemName);

        int dbCount = tableItem.getDbCount();

        //某一个库该tableItem所有的表, 如["table_0",...,"table_12"]
        List<String> tableFullNameList = TableItemHelper.getAllTableNameInOneDatabase(tableItem);
        Map<Integer, String> dbIndexAndNameMap = TableItemHelper.getAllDbIndexAndNameMap(tableItem);

        JdbcTemplate[] jdbcTemplates = tableItem.getJdbcTemplates();
        if (jdbcTemplates == null) {
            return null;
        }


        List<TableChannel> tableChannels = new ArrayList<TableChannel>();
        int jdbcTemplateSize = jdbcTemplates.length;

        Iterator<Map.Entry<Integer, String>> dbNameIterator = dbIndexAndNameMap.entrySet().iterator();

        while (dbNameIterator.hasNext()) {
            Map.Entry<Integer, String> next = dbNameIterator.next();
            int dbIndex = next.getKey();
            String dbFullName = next.getValue();

            JdbcTemplate jdbcTemplate = jdbcTemplates[dbIndex / (dbCount / jdbcTemplateSize)];

            for (String tableFullName : tableFullNameList) {
                String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);

                TableChannel tableChannel = channelMap.get(sqlKey);
                if (tableChannel == null) {
                    tableChannel = new TableChannel();
                    String sql = TableItemHelper.getSql(tableItem, sqlName, dbFullName, tableFullName);
                    tableChannel.setSql(sql);
                    tableChannel.setJdbcTemplate(jdbcTemplate);
                    tableChannel.setId(atomicLong.incrementAndGet() + System.currentTimeMillis());
                    channelMap.put(sqlKey, tableChannel);
                }
                tableChannels.add(tableChannel);
            }

        }
        return tableChannels;
    }


    public Map<String, TableItem> getTableItems() {
        return tableItems;
    }

    public void setTableItems(Map<String, TableItem> tableItems) {
        this.tableItems = tableItems;
    }
}
