package com.liuyiling.coding.mysql.table;

import com.liuyiling.coding.mysql.jdbc.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 存放具体的TableItem 和 TableContainer
 * 所有Dao类都需要继承该类
 * Created by liuyl on 2016/12/29.
 */
public class TableContainer {

    //key:spring配置中的tableItem名字,value:具体的tableItem实例
    public Map<String, TableItem> tableItems;
    //key:对应具体库的SQL语句,value:具体的TableChannel连接,作用:连接复用
    private static Map<String, TableChannel> channelMap = new ConcurrentHashMap<>();

    private static AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 根据spring配置中的table_item_name获取对应的TableItem
     * @param tableItemName
     * @return
     */
    public TableItem getTableItem(String tableItemName) {
        if (!CollectionUtils.isEmpty(tableItems)) {
            return tableItems.get(tableItemName);
        }
        return null;
    }

    /**
     * 获取SQL操作所依赖的SQL连接
     * 该方法适用于单库单表的情况,大多数情况下单库单表已经能解决问题了
     * 分库分表一定要细细斟酌
     * @param tableItemName
     * @param sqlName
     * @return
     */
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

    /**
     * 按照strategyId分库,按照日期分表
     * 获取SQL操作所依赖的SQL连接
     * @param tableItemName
     * @param sqlName
     * @param dbStrategyId
     * @param date
     * @return
     */
    public TableChannel getTableChannel(String tableItemName, String sqlName, Long dbStrategyId, Date date) {
        TableItem tableItem = this.getTableItem(tableItemName);
        String tableFullName = TableItemHelper.getTableFullName(tableItem, date);
        String dbFullName = TableItemHelper.getDbFullName(tableItem, dbStrategyId);
        String sqlKey = TableItemHelper.getSqlKey(dbFullName, tableFullName, sqlName);
        TableChannel channel = channelMap.get(sqlKey);
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

    /**
     * 按照dbStrategyId分库,按照tableStrategyId分表
     * 获取SQL操作所依赖的SQL连接
     * @param tableItemName
     * @param sqlName
     * @param dbStrategyId
     * @param tableStrategyId
     * @return
     */
    public TableChannel getTableChannel(String tableItemName, String sqlName, Long dbStrategyId, Long tableStrategyId) {
        TableItem tableItem = getTableItem(tableItemName);

        String tableFullName = TableItemHelper.getTableFullName(tableItem, tableStrategyId);
        String dbFullName = TableItemHelper.getDbFullName(tableItem, dbStrategyId);
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

    /**
     * 获取该SQL语句的所有连接
     * 可以理解为对数据源做一次全扫描
     * @param tableItemName
     * @param sqlName
     * @return
     */
    public List<TableChannel> getAllTableChannel(String tableItemName, String sqlName) {
        TableItem tableItem = this.getTableItem(tableItemName);

        int dbCount = tableItem.getDbCount();

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
