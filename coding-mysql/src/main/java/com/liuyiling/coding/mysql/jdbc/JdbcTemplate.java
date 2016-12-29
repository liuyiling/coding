package com.liuyiling.coding.mysql.jdbc;

import com.liuyiling.common.util.TimeStatUtil;
import com.liuyiling.common.util.UniversalLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuyl on 2016/12/29.
 */
public class JdbcTemplate extends JdbcAccessor {

    private static final String MYSQL_RESOURCE_TYPE = "mysql";

    private DataSource master;
    private List<DataSource> slavers = new ArrayList<>();
    private int readTryTimes = 3;
    private int writeTryTimes = 3;
    private int resource;
    private int port;

    private int fetchSize = 0;
    private int maxRows = 0;
    private int queryTimeout = 0;
    private boolean ignoreWarnings = true;

    private Random random = new Random();

    //----------------------------------------------------------------------------------
    //------------------------------update操作方法---------------------------------------
    //----------------------------------------------------------------------------------
    public int update(String sql, Object[] args) throws DataAccessException {
        return update(sql, new ArgPreparedStatementSetter(args));
    }

    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        return update(new SimplePreparedStatementCreator(sql), pss);
    }

    protected int update(final PreparedStatementCreator psc, final PreparedStatementSetter pss) throws DataAccessException {

        if(UniversalLogger.isTraceEnabled()){
            UniversalLogger.trace("Executing prepared SQL update");
        }

        Integer result = (Integer) execute(psc, new PreparedStatementCallback() {
            public Object doInPreparedStatement(PreparedStatement ps) throws SQLException {
                try {
                    if (pss != null) {
                        pss.setValues(ps);
                    }
                    int rows = ps.executeUpdate();
                    if (UniversalLogger.isTraceEnabled()) {
                        UniversalLogger.trace("SQL update affected " + rows + " rows");
                    }
                    return new Integer(rows);
                }
                finally {
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            }
        }, true);
        
        return result.intValue();
    }

    public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder) throws DataAccessException {

        Integer result = (Integer) execute(psc, new PreparedStatementCallback() {
            public Object doInPreparedStatement(PreparedStatement ps) throws SQLException {
                int rows = ps.executeUpdate();
                List generatedKeys = generatedKeyHolder.getKeyList();
                generatedKeys.clear();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys != null) {
                    try {
                        RowMapper rowMapper = getColumnMapRowMapper();
                        RowMapperResultSetExtractor rse = new RowMapperResultSetExtractor(rowMapper, 1);
                        generatedKeys.addAll((List) rse.extractData(keys));
                    }
                    finally {
                        JdbcUtils.closeResultSet(keys);
                    }
                }
                return new Integer(rows);
            }
        }, true);
        return result.intValue();
    }
    
    /**
     * 处理SQL语句的方法
     */

    public Object execute(PreparedStatementCreator psc, PreparedStatementCallback action, boolean isWrite)
            throws DataAccessException {

        if (UniversalLogger.isTraceEnabled()) {
            String sql = getSql(psc);
            UniversalLogger.trace(new StringBuilder(128).append("Executing prepared SQL statement").append((sql != null ? " [" + sql + "]" : "")).toString());
        }

        long start = System.currentTimeMillis();
        DataSource ds = getDataSource(isWrite);
        if(ds == null){
            return null;
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            //finally里的日志统计应包含所有请求，而不只是获取到connection的请求
            con = safeGetConnection(ds, isWrite);
            Connection conToUse = con;
            ps = psc.createPreparedStatement(conToUse);
            applyStatementSettings(ds, ps);
            PreparedStatement psToUse = ps;
            Object result = action.doInPreparedStatement(psToUse);
            handleWarnings(ps);
            return result;
        }catch(CannotGetJdbcConnectionException e){
            throw e;
        }
        catch (Exception ex) {
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            String sql = getSql(psc);
            psc = null;
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, ds);
            con = null;
            if (ex instanceof SQLException) {
                throw getExceptionTranslator(ds).translate("PreparedStatementCallback", sql, (SQLException)ex);
            } else {
                throw new RuntimeException("PreparedStatementCallback " + getSql(psc), ex);
            }
        }
        finally {

            long end = System.currentTimeMillis();
            long useTime = end -start;
            if(useTime > UniversalLogger.DB_FIRE_TIME){
                UniversalLogger.fire(new StringBuffer().append("DB ").append(((com.mchange.v2.c3p0.ComboPooledDataSource)ds).getJdbcUrl()).append(" too slow :").append(useTime).append(" isWrite:").append(isWrite).toString());
            }
            String[] jdbcUrl = ((com.mchange.v2.c3p0.ComboPooledDataSource)ds).getJdbcUrl().split("/");
            if(jdbcUrl != null && jdbcUrl.length > 2){
            }

            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, ds);

            TimeStatUtil.addElapseTimeStat(resource, isWrite, start, useTime);
        }
    }

    public Object execute(String sql, PreparedStatementCallback action, boolean isWrite) throws DataAccessException {
        return execute(new SimplePreparedStatementCreator(sql), action, isWrite);
    }
    
    
    public Object execute(StatementCallback action, boolean isWrite) throws DataAccessException {
        long startTime = System.currentTimeMillis();

        DataSource dataSource = getDataSource(isWrite);
        if (dataSource == null) {
            return null;
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = safeGetConnection(dataSource, isWrite);

            stmt = conn.createStatement();
            Statement stmtToUse = stmt;

            Object result = action.doInStatement(stmtToUse);
            handleWarnings(stmt);
            return result;
        } catch (CannotGetJdbcConnectionException e) {
            throw e;
        } catch (Exception e) {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(conn, dataSource);
            if (e instanceof SQLException) {
                throw getExceptionTranslator(dataSource).translate("StatementCallback", getSql(action), (SQLException) e);
            } else {
                throw new RuntimeException("StatementCallback " + getSql(action), e);
            }
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(conn, dataSource);

            long end = System.currentTimeMillis();
            long useTime = end - startTime;
            if(useTime > UniversalLogger.DB_FIRE_TIME){
                UniversalLogger.fire(new StringBuffer().append("DB ").append(((com.mchange.v2.c3p0.ComboPooledDataSource)dataSource).getJdbcUrl()).append(" too slow :").append(useTime).append(" isWrite:").append(isWrite).toString());
            }

            TimeStatUtil.addElapseTimeStat(resource, isWrite, startTime, useTime);
        }
    }

    //----------------------------------------------------------------------------------
    //------------------------------query操作方法----------------------------------------
    //----------------------------------------------------------------------------------
    public List query(String sql, RowMapper rowMapper) throws DataAccessException {
        return (List) query(sql, new RowMapperResultSetExtractor(rowMapper));
    }

    public List query(String sql, Object[] args, RowMapper rowMapper) throws DataAccessException {
        return (List) query(sql, args, new RowMapperResultSetExtractor(rowMapper));
    }

    public Object query(String sql, Object[] args, ResultSetExtractor rse) throws DataAccessException {
        return query(sql, new ArgPreparedStatementSetter(args), rse);
    }

    public Object query(String sql, PreparedStatementSetter pss, ResultSetExtractor rse) throws DataAccessException {
        return query(new SimplePreparedStatementCreator(sql), pss, rse);
    }

    public Object query(
            PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor rse)
            throws DataAccessException {

        return execute(psc, ps -> {
            ResultSet rs = null;
            try {
                if (pss != null) {
                    pss.setValues(ps);
                }
                rs = ps.executeQuery();
                ResultSet rsToUse = rs;
                return rse.extractData(rsToUse);
            }
            finally {
                JdbcUtils.closeResultSet(rs);
                if (pss instanceof ParameterDisposer) {
                    ((ParameterDisposer) pss).cleanupParameters();
                }
            }
        }, false);
    }

    public Object query(final String sql, final ResultSetExtractor rse) throws DataAccessException {

        if (UniversalLogger.isTraceEnabled()) {
            UniversalLogger.trace(new StringBuilder(64).append("Executing SQL query [").append(sql).append("]").toString());
        }

        class QueryStatementCallback implements StatementCallback, SqlProvider {
            public Object doInStatement(Statement stmt) throws SQLException {
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery(sql);
                    ResultSet rsToUse = rs;
                    return rse.extractData(rsToUse);
                }
                finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }
            public String getSql() {
                return sql;
            }
        }
        return execute(new QueryStatementCallback(), false);
    }

    private DataSource getDataSource(boolean isWrite) throws DataAccessException {

        if (isWrite || CollectionUtils.isEmpty(slavers)) {
            return master;
        } else {
            if (slavers.size() == 1) {
                return slavers.get(0);
            } else {
                int rd = random.nextInt(slavers.size());
                return slavers.get(rd);
            }
        }
    }

    private Connection safeGetConnection(DataSource dataSource, boolean isWrite) throws CannotGetJdbcConnectionException {
        Connection conn = null;
        int retryCount;
        int count;
        retryCount = isWrite ? writeTryTimes : readTryTimes;
        count = retryCount;

        while (count > 0) {
            long startTime = System.currentTimeMillis();
            try {
                conn = DataSourceUtils.getConnection(dataSource);
                return conn;
            } catch (CannotGetJdbcConnectionException e) {
                UniversalLogger.warn(new StringBuilder(64).append("get connection try count:").append((retryCount - count))
                        .append(", ds=").append(((com.mchange.v2.c3p0.ComboPooledDataSource) dataSource).getJdbcUrl())
                        + ",time:" + (System.currentTimeMillis() - startTime), e);
                DataSourceUtils.releaseConnection(conn, dataSource);
            }
        }

        UniversalLogger.fire(new StringBuffer().append("DB ").append(((com.mchange.v2.c3p0.ComboPooledDataSource) dataSource).getJdbcUrl()).append(" Error:").append("Could not get JDBC Connection: ").toString());
        throw new CannotGetJdbcConnectionException("Could not get JDBC Connection: " + ", ds=" + ((com.mchange.v2.c3p0.ComboPooledDataSource) dataSource).getJdbcUrl(), new SQLException());
    }

    protected void applyStatementSettings(DataSource dataSource, Statement stmt) throws SQLException {
        int fetchSize = getFetchSize();
        if (fetchSize > 0) {
            stmt.setFetchSize(fetchSize);
        }
        int maxRows = getMaxRows();
        if (maxRows > 0) {
            stmt.setMaxRows(maxRows);
        }
        DataSourceUtils.applyTimeout(stmt, dataSource, getQueryTimeout());
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    /**
     * 获得mysql的地址
     *
     * @param jdbcUrl jdbc:mysql://172.16.200.11:3306/?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull
     * @return
     */
    private String getJdbcUrl(String jdbcUrl) {
        String jdbcPrefix = "jdbc:mysql://";
        if (!jdbcUrl.startsWith(jdbcPrefix)) {
            return jdbcUrl;
        }

        int portIndex = jdbcUrl.indexOf(":", jdbcPrefix.length());
        if (portIndex > 0) {
            return jdbcUrl.substring(jdbcPrefix.length(), portIndex);
        } else {
            return jdbcUrl;
        }
    }

    public void setMaster(DataSource master) {
        this.master = master;

        com.mchange.v2.c3p0.ComboPooledDataSource c3p0DataSource = (com.mchange.v2.c3p0.ComboPooledDataSource) master;
        String url = c3p0DataSource.getJdbcUrl();
        if (StringUtils.hasText(url)) {
            String portStr = url.split(":")[3].split("/")[0];
            port = Integer.parseInt(portStr);
            resource = TimeStatUtil.DB_TYPE + port;
            TimeStatUtil.register(resource);
        } else {
            throw new RuntimeException("empty jdbc url string in master slave");
        }
    }

    protected void handleWarnings(Statement stmt) throws SQLException {
        if (isIgnoreWarnings()) {
            if (UniversalLogger.isTraceEnabled()) {
                SQLWarning warningToLog = stmt.getWarnings();
                while (warningToLog != null) {
                    UniversalLogger.trace("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" +
                            warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
                    warningToLog = warningToLog.getNextWarning();
                }
            }
        } else {
            handleWarnings(stmt.getWarnings());
        }
    }

    protected void handleWarnings(SQLWarning warning) throws SQLWarningException {
        if (warning != null) {
            throw new SQLWarningException("Warning not ignored", warning);
        }
    }

    public DataSource getMaster() {
        return master;
    }

    public int getPort() {
        return port;
    }

    public void setSlavers(List<DataSource> slavers) {
        this.slavers = slavers;

        String portStr = ((com.mchange.v2.c3p0.ComboPooledDataSource) slavers.get(0)).getJdbcUrl().split(":")[3].split("/")[0];
        port = Integer.parseInt(portStr);
        resource = TimeStatUtil.DB_TYPE + port;
        TimeStatUtil.register(resource);
    }

    /**
     * Create a new RowMapper for reading columns as key-value pairs.
     * @return the RowMapper to use
     * @see ColumnMapRowMapper
     */
    protected RowMapper getColumnMapRowMapper() {
        return new ColumnMapRowMapper();
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        super.setLazyInit(lazyInit);
    }

    @Override
    public boolean isLazyInit() {
        return super.isLazyInit();
    }

    @Override
    public void afterPropertiesSet(DataSource dataSource) {
        super.afterPropertiesSet(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    @Override
    public SQLExceptionTranslator getExceptionTranslator(DataSource dataSource) {
        return null;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public boolean isIgnoreWarnings() {
        return ignoreWarnings;
    }

    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }
}
