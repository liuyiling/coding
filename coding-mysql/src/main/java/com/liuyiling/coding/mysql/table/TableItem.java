package com.liuyiling.coding.mysql.table;

import com.liuyiling.coding.mysql.jdbc.JdbcTemplate;

import java.io.Serializable;
import java.util.Map;

/**
 * 描述具体某张表的module
 * Created by liuyl on 2016/12/29.
 */
public class TableItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private JdbcTemplate[] jdbcTemplates;
    private Map<String, String> sqls;
    private String tableNamePrefix;
    private String tableNamePostFix;
    private String dbNamePrefix;
    private String dbNamePostFix;
    private int tableCount = 0;
    private int dbCount = 0;
    private String itemName;
    private Map<String, JdbcTemplate[]> jdbcTemplateCluster;
    private boolean hierarchy = false;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((itemName == null) ? 0 : itemName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableItem other = (TableItem) obj;
        if (itemName == null) {
            if (other.itemName != null)
                return false;
        } else if (!itemName.equals(other.itemName))
            return false;
        return true;
    }

    public JdbcTemplate[] getJdbcTemplates() {
        return jdbcTemplates;
    }

    public void setJdbcTemplates(JdbcTemplate[] jdbcTemplates) {
        this.jdbcTemplates = jdbcTemplates;
    }

    public Map<String, String> getSqls() {
        return sqls;
    }

    public void setSqls(Map<String, String> sqls) {
        this.sqls = sqls;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public String getTableNamePostFix() {
        return tableNamePostFix;
    }

    public void setTableNamePostFix(String tableNamePostFix) {
        this.tableNamePostFix = tableNamePostFix;
    }

    public String getDbNamePrefix() {
        return dbNamePrefix;
    }

    public void setDbNamePrefix(String dbNamePrefix) {
        this.dbNamePrefix = dbNamePrefix;
    }

    public String getDbNamePostFix() {
        return dbNamePostFix;
    }

    public void setDbNamePostFix(String dbNamePostFix) {
        this.dbNamePostFix = dbNamePostFix;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public int getDbCount() {
        return dbCount;
    }

    public void setDbCount(int dbCount) {
        this.dbCount = dbCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Map<String, JdbcTemplate[]> getJdbcTemplateCluster() {
        return jdbcTemplateCluster;
    }

    public void setJdbcTemplateCluster(Map<String, JdbcTemplate[]> jdbcTemplateCluster) {
        this.jdbcTemplateCluster = jdbcTemplateCluster;
    }

    public boolean isHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }
}
