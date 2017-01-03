package com.liuyiling.coding.mysql.table;

import com.liuyiling.coding.mysql.jdbc.JdbcTemplate;
import com.liuyiling.common.helper.UuidHelper;
import com.liuyiling.common.util.ApiUtil;
import com.liuyiling.common.util.UniversalLogger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.*;

public final class TableItemHelper {
    private static final String DB_NAME_EXPRESSION = "$db$";
    private static final String TABLE_NAME_EXPRESSION = "$tb$";

    public enum DbNamePostfixType {
        index("index"), none("none"), index_full("index_full");
        private String type;

        DbNamePostfixType(String type) {
            this.type = type;
        }

        public String value() {
            return type;
        }
    }

    public enum TNamePostfixType {
        yyMM("yymm"), yyMMdd("yymmdd"), index("index"), index_full("index_full"), index_full_medias("index_full_medias");
        private String type;

        TNamePostfixType(String type) {
            this.type = type;
        }

        public String value() {
            return type;
        }
    }

    public static JdbcTemplate[] chooseJdbcTemplates(TableItem item, Long id) {
        if (item.isHierarchy() == false) {
            return item.getJdbcTemplates();
        }

        Date date = UuidHelper.getDateFromId(id);
        String year = DateFormatUtils.format(date, "yyyy");
        Map<String, JdbcTemplate[]> cluster = item.getJdbcTemplateCluster();
        JdbcTemplate[] templates = cluster.get(year);
        if (ArrayUtils.isEmpty(templates)) {
            UniversalLogger.error("!!!!!!!Can not get correct template, fall down to choose backup template!!!!!!, id="
                    + String.valueOf(id));
            return item.getJdbcTemplates();
        }

        return templates;
    }

    public static JdbcTemplate getJdbcTemplate(TableItem item, Long dbStrategyId) {
        if (item != null && dbStrategyId != null) {
            JdbcTemplate[] templates = chooseJdbcTemplates(item, dbStrategyId);

            if (ArrayUtils.isNotEmpty(templates)) {
                int index = getDbIndexById(item, dbStrategyId);
                index = index == -1 ? 0 : index / (item.getDbCount() / templates.length);
                return templates[index];
            } else {
                UniversalLogger.error(StringUtils.defaultIfEmpty(item.getTableNamePrefix(), "") + " jdbcTemplates is empty or null");
            }

        } else {
            UniversalLogger.error("tableitem is null");

        }
        return null;
    }

    public static String getSql(TableItem item, String sqlName, Long dbId,
                                Long tbId) {
        String dbName = getDbFullName(item, dbId);
        String tableName = getTableFullName(item, tbId);

        return getSql(item, sqlName, dbName, tableName);
    }

    /**
     * 使用dbName, tableName替换sql里的$db$,$tb$字段
     *
     * @param item
     * @param sqlName
     * @param dbName
     * @param tableName
     * @return
     */
    public static String getSql(TableItem item, String sqlName, String dbName, String tableName) {
        String sql = item.getSqls().get(sqlName);
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.replace("$db$", dbName);
            sql = sql.replace("$tb$", tableName);

        } else {
            UniversalLogger.error("find the sql by name " + sqlName
                    + " is empty or null");
        }
        if (UniversalLogger.isDebugEnabled()) {
            UniversalLogger.debug(sql);
        }
        return sql;
    }

    /**
     * @param sqlName
     * @return
     */
    public static String getSql(TableItem item, String sqlName) {
        String tableName = item.getTableNamePrefix();
        String dbName = item.getDbNamePrefix();
        String sql = item.getSqls().get(sqlName);
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.replace("$db$", dbName);
            sql = sql.replace("$tb$", tableName);

        } else {
            UniversalLogger.error("find the sql by name " + sqlName
                    + " is empty or null");
        }

        if (UniversalLogger.isDebugEnabled()) {
            UniversalLogger.debug(sql);
        }
        return sql;

    }

    /**
     * @param sqlName
     * @return
     */
    public static String getSql(TableItem item, String sqlName, Long dbId,
                                Date date) {
        String tableName = getTableFullName(item, date);
        String dbName = getDbFullName(item, dbId);
        String sql = item.getSqls().get(sqlName);
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.replace(DB_NAME_EXPRESSION, dbName);
            sql = sql.replace(TABLE_NAME_EXPRESSION, tableName);

        } else {
            UniversalLogger.error("find the sql by name " + sqlName
                    + " is empty or null");
        }
        if (UniversalLogger.isDebugEnabled()) {
            UniversalLogger.debug(sql);
        }
        return sql;

    }

    /**
     * 根据策略ID 获取数据库的全名
     * @param item
     * @param dbStrategyId
     * @return
     */
    public static String getDbFullName(TableItem item, Long dbStrategyId) {
        if (item == null) {
            UniversalLogger.error("tableitem is null");
            return null;
        }

        //默认配置数据库为索引分表 db_0,db_1,...,db_7
        if (DbNamePostfixType.index.value().equals(item.getDbNamePostFix()) || StringUtils.isBlank(item.getDbNamePostFix())) {
            return getIndexDbNameById(item, dbStrategyId);

        } else if (DbNamePostfixType.index_full.value().equals(item.getDbNamePostFix())) {
            return getFullIndexDbNameById(item, dbStrategyId);

        } else if (DbNamePostfixType.none.value().equals(item.getDbNamePostFix())) {
            return item.getDbNamePrefix();

        } else {
            return null;

        }
    }

    /**
     * 得到所有库的名字
     *
     * @param tableItem
     * @return
     */
    public static Map<Integer, String> getAllDbIndexAndNameMap(TableItem tableItem) {

        Map<Integer, String> dbIndexNameMap = new HashMap<Integer, String>();
        int dbCount = tableItem.getDbCount();
        String dbNamePostFix = tableItem.getDbNamePostFix();

        for (int dbIndex = 0; dbIndex < dbCount; dbIndex++) {

            String postSuffix = "";

            if (DbNamePostfixType.index.value().equals(dbNamePostFix) || StringUtils.isBlank(dbNamePostFix)) {
                //没配置就是index，形如table_0, table_1,table_12
                postSuffix = "_" + dbIndex;

            } else if (DbNamePostfixType.index_full.value().equals(dbNamePostFix)) {
                //就是数字前不全，形如table_00,table_01,table_12
                postSuffix = "_" + padsZero(dbIndex, dbCount);

            } else if (DbNamePostfixType.none.value().equals(dbNamePostFix)) {
                postSuffix = "";

            }

            String dbFullName = tableItem.getDbNamePrefix() + postSuffix;
            dbIndexNameMap.put(dbIndex, dbFullName);

        }

        return dbIndexNameMap;
    }


    private static String getIndexDbNameById(TableItem item, Long id) {
        if (StringUtils.isNotBlank(item.getDbNamePrefix()) && id != null) {
            int dbIndex = 0;

            dbIndex = getDbIndexById(item, id);

            return new StringBuilder().append(item.getDbNamePrefix()).append("_").append(dbIndex).toString();

        } else {
            UniversalLogger.error("dbNamePrefix is null or empty, id is null");
            return null;

        }
    }

    private static String getFullIndexDbNameById(TableItem item, Long id) {
        if (StringUtils.isNotBlank(item.getDbNamePrefix()) && id != null) {
            StringBuilder fullName = new StringBuilder(item.getDbNamePrefix()).append("_");
            fullName.append(padsZero(getDbIndexById(item, id), item.getDbCount()));
            return fullName.toString();

        } else {
            UniversalLogger.error("dbNamePrefix is null or empty, id is null");
            return null;
        }
    }


    /**
     * 根据给定的dbStrategyId，得到它应该是在哪个数据库实例中
     *
     * @param item
     * @param dbStrategyId
     * @return
     */
    private static int getDbIndexById(TableItem item, Long dbStrategyId) {
        if (DbNamePostfixType.index.value().equals(item.getDbNamePostFix()) || StringUtils.isBlank(item.getDbNamePostFix())) {
            //没配置就是index
            return ApiUtil.getHash4split(dbStrategyId, item.getDbCount());

        } else if (DbNamePostfixType.index_full.value().equals(item.getDbNamePostFix())) {
            return (int) (dbStrategyId % item.getDbCount());

        } else if (DbNamePostfixType.none.value().equals(item.getDbNamePostFix())) {
            return -1;

        } else {
            UniversalLogger.error("getDbIndexById dbnamepostfix[" + item.getDbNamePostFix() + "] not support");
            return -1;

        }
    }

    public static String getTableFullName(TableItem item, Long strategyId) {
        if (item != null) {
            if (TNamePostfixType.yyMM.value().equals(item.getTableNamePostFix())) {
                return getDateTableNameById(item.getTableNamePrefix(), strategyId, false);

            } else if (TNamePostfixType.yyMMdd.value().equals(item.getTableNamePostFix())) {
                return getDateTableNameById(item.getTableNamePrefix(), strategyId, true);

            } else if (TNamePostfixType.index.value().equals(item.getTableNamePostFix())) {
                //形如table_0, table_1,...,table_12
                return getIndexTableNameById(item, strategyId);

            } else if (TNamePostfixType.index_full.value().equals(item.getTableNamePostFix())) {
                //形如table_00,table_01,...,table_12
                return getFullIndexTableNameById(item, strategyId);

            } else if (TNamePostfixType.index_full_medias.value().equals(item.getTableNamePostFix())) {
                return getFullIndexTableNameByIdLastTwoPoint(item, strategyId);

            } else { //如果没有匹配到, 就按index处理
                return getIndexTableNameById(item, strategyId);

            }

        } else {
            UniversalLogger.error("tableitem is null");
            return null;

        }

    }

    /**
     * 得到某个库中该tableItem所有的表
     *
     * @return
     */
    public static List<String> getAllTableNameInOneDatabase(TableItem tableItem) {
        String tableNamePostFix = tableItem.getTableNamePostFix();
        if (!TableItemHelper.TNamePostfixType.index.value().equals(tableNamePostFix) &&
                !TableItemHelper.TNamePostfixType.index_full.value().equals(tableNamePostFix) &&
                !TableItemHelper.TNamePostfixType.index_full_medias.value().equals(tableNamePostFix)) {
            throw new IllegalArgumentException("getAllTableChannel only support index,index_full,index_full_medias table type");
        }

        List<String> list = new ArrayList<String>();
        for (int tableIndex = 0; tableIndex < tableItem.getTableCount(); tableIndex++) {
            String suffix = "";
            if (TableItemHelper.TNamePostfixType.index.value().equals(tableNamePostFix)) {
                suffix = "" + tableIndex;
            } else if (TableItemHelper.TNamePostfixType.index_full.value().equals(tableNamePostFix)) {
                suffix = padsZero(tableIndex, tableItem.getTableCount());
            } else if (TableItemHelper.TNamePostfixType.index_full_medias.value().equals(tableNamePostFix)) {
                suffix = padsZero(tableIndex, tableItem.getTableCount());
            }

            String tableFullName = tableItem.getTableNamePrefix() + "_" + suffix;
            list.add(tableFullName);
        }

        return list;

    }


    /**
     * 获取按日期分表的表全名
     * @param item
     * @param date
     * @return
     */
    public static String getTableFullName(TableItem item, Date date) {
        if (item != null) {
            if (TNamePostfixType.yyMM.value().equals(item.getTableNamePostFix())) {
                return getDateTableNameByDate(item.getTableNamePrefix(), date, false);

            } else if (TNamePostfixType.yyMMdd.value().equals(item.getTableNamePostFix())) {
                return getDateTableNameByDate(item.getTableNamePrefix(), date, true);

            }
        }

        UniversalLogger.error("tableitem is null");
        return null;
    }

    /**
     * @param tblPrefix
     * @param id
     * @return
     */
    private static String getDateTableNameById(String tblPrefix, Long id, boolean isDisplayDay) {
        if (StringUtils.isNotBlank(tblPrefix) && id != null) {
            Long milliseconds = UuidHelper.getTimeFromId(id);
            String yyMMdd = DateFormatUtils.format(milliseconds, isDisplayDay ? "yyMMdd" : "yyMM");
            return new StringBuilder().append(tblPrefix).append("_").append(yyMMdd).toString();

        } else {
            UniversalLogger.error("tblPrefix is null or empty, id is null");
            return null;

        }
    }

    /**
     * 按照日期获得具体表名
     * @param tblPrefix
     * @param date
     * @return
     */
    private static String getDateTableNameByDate(String tblPrefix, Date date, boolean isDisplayDay) {
        if (StringUtils.isNotBlank(tblPrefix) && date != null) {
            String yyMMdd = DateFormatUtils.format(date, isDisplayDay ? "yyMMdd" : "yyMM");
            return new StringBuilder().append(tblPrefix).append("_").append(yyMMdd).toString();

        } else {
            UniversalLogger.error("tblPrefix is null or empty, date is null");
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    private static String getIndexTableNameById(TableItem item, Long id) {
        if (StringUtils.isNotBlank(item.getTableNamePrefix()) && id != null
                && item.getTableCount() > 0 && item.getDbCount() > 0) {
            int tblIndex = ApiUtil.getHash4split(id, item.getDbCount() * item.getTableCount());

            tblIndex = tblIndex / item.getDbCount();

            return new StringBuilder().append(item.getTableNamePrefix()).append("_").append(tblIndex).toString();

        } else {
            UniversalLogger.error("tblPrefix is null or empty, id is null");
            return null;

        }
    }

    /**
     * 根据具体的策略id获得表全名
     *
     */
    private static String getFullIndexTableNameById(TableItem item, Long strategyId) {
        if (StringUtils.isNotBlank(item.getTableNamePrefix()) && strategyId != null && item.getTableCount() > 0) {
            StringBuilder fullName = new StringBuilder(item.getTableNamePrefix()).append("_");
            int tblIndex = (int) (strategyId % item.getTableCount());
            fullName.append(padsZero(tblIndex, item.getTableCount()));
            return fullName.toString();

        } else {
            UniversalLogger.error("tblPrefix is null or empty, id is null");
            return null;

        }
    }

    /**
     * 根据id的后两位来进行分表
     */
    private static String getFullIndexTableNameByIdLastTwoPoint(TableItem item, Long id) {
        if (StringUtils.isNotBlank(item.getTableNamePrefix()) && id != null && item.getTableCount() > 0) {
            StringBuilder fullName = new StringBuilder(item.getTableNamePrefix()).append("_");
            int tblIndex = (int) (id / 100 % item.getTableCount());
            fullName.append(padsZero(tblIndex, item.getTableCount()));
            return fullName.toString();

        } else {
            UniversalLogger.error("tblPrefix is null or empty, id is null");
            return null;

        }
    }

    private static String padsZero(int index, int totalCount) {
        StringBuilder sb = new StringBuilder();
        int zeroCount = String.valueOf(totalCount - 1).length() - String.valueOf(index).length();
        for (int i = 0; i < zeroCount; i++) {
            sb.append("0");
        }
        sb.append(index);
        return sb.toString();
    }

    /**
     * 组装Sql操作语句
     * @param dbName
     * @param tableName
     * @param sqlName
     * @return
     */
    public static String getSqlKey(String dbName, String tableName, String sqlName) {
        String sqlCacheKey = new StringBuffer().append(dbName).append(".")
                .append(tableName).append(".").append(sqlName).toString();
        return sqlCacheKey.toString();
    }

}