package com.liuyiling.common.spring;

import com.liuyiling.common.util.UniversalLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.lang.StringUtils;

/**
 * Created by liuyl on 2017/1/1.
 */
public class MysqlClientFactoryBean extends CacheableObjectFactoryBean<ComboPooledDataSource> {

    private static final String KEY_PREFIX = "MYSQL_";

    private String driverClass = "com.mysql.jdbc.Driver";
    private String jdbcUrl;
    private String user;
    private String password;
    private int minPoolSize = 2;
    private int maxPoolSize = 50;
    private int idleConnectionTestPeriod = 25;
    private int maxIdleTime = 180;
    private boolean breakAfterAcquireFailure = false;
    private int checkoutTimeout;
    private int acquireRetryAttempts = 1;
    private int acquireRetryDelay = 500;
    private int maxConnectionAge = 1200;

    MysqlConfigStrategy strategy = MysqlConfigStrategy.master;

    public void setStrategy(MysqlConfigStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected StringBuilder getKey() {
        StringBuilder buf = super.getKey();
        buf.append("-").append(jdbcUrl).append("-").append(user);
        return buf;
    }

    @Override
    protected String getKeyPrefix() {
        return KEY_PREFIX;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getIdleConnectionTestPeriod() {
        return idleConnectionTestPeriod;
    }

    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
        this.idleConnectionTestPeriod = idleConnectionTestPeriod;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public boolean isBreakAfterAcquireFailure() {
        return breakAfterAcquireFailure;
    }

    public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
        this.breakAfterAcquireFailure = breakAfterAcquireFailure;
    }

    public int getCheckoutTimeout() {
        return checkoutTimeout;
    }

    public void setCheckoutTimeout(int checkoutTimeout) {
        this.checkoutTimeout = checkoutTimeout;
    }

    public int getAcquireRetryAttempts() {
        return acquireRetryAttempts;
    }

    public void setAcquireRetryAttempts(int acquireRetryAttempts) {
        this.acquireRetryAttempts = acquireRetryAttempts;
    }

    public int getAcquireRetryDelay() {
        return acquireRetryDelay;
    }

    public void setAcquireRetryDelay(int acquireRetryDelay) {
        this.acquireRetryDelay = acquireRetryDelay;
    }

    public int getMaxConnectionAge() {
        return maxConnectionAge;
    }

    public void setMaxConnectionAge(int maxConnectionAge) {
        this.maxConnectionAge = maxConnectionAge;
    }

    public static enum MysqlConfigStrategy {
        master(XmlConstants.MYSQL_DEFAULT_MASTER_CHECK_OUT_TIMEOUT,
                XmlConstants.MYSQL_DEFAULT_MASTER_SOCKET_TIMEOUT,
                XmlConstants.MYSQL_DEFAULT_MASTER_SOCKET_CONNECT_TIMEOUT),
        slave(XmlConstants.MYSQL_DEFAULT_SLAVE_CHECK_OUT_TIMEOUT,
                XmlConstants.MYSQL_DEFAULT_SLAVE_SOCKET_TIMEOUT,
                XmlConstants.MYSQL_DEFAULT_SLAVE_SOCKET_CONNECT_TIMEOUT);
        private int checkoutTimeout;
        private int socketTimeout;
        private int connectTimeout;

        private MysqlConfigStrategy(int checkoutTimeout, int socketTimeout, int connectTimeout) {
            this.checkoutTimeout = checkoutTimeout;
            this.socketTimeout = socketTimeout;
            this.connectTimeout = connectTimeout;
        }

        public void setTimeoutConfig(ComboPooledDataSource dataSource) {
            if (dataSource.getCheckoutTimeout() <= 0) {
                dataSource.setCheckoutTimeout(checkoutTimeout);
            }
            String jdbcUrl = dataSource.getJdbcUrl();
            StringBuilder sb = new StringBuilder(jdbcUrl);
            /*
             * 如果自己在mysql.properties中的jdbcUrl已配置则按着此配置，否则按枚举类型里的默认配置
             * 为方便管理与维护，如需自己配置，只支持jdbcUrl配置socketTimeout、connectTimeout
             */
            if (!jdbcUrl.contains("socketTimeout")) {
                // 如果jdbcUrl中无参，则用?来接语句，否则用&
                if (jdbcUrl.contains("?")) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append("socketTimeout=").append(socketTimeout);
            }
            if (!jdbcUrl.contains("connectTimeout")) {
                if (sb.toString().contains("?")) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append("connectTimeout=").append(connectTimeout);
            }
            dataSource.setJdbcUrl(sb.toString());
        }
    }

    @Override
    protected Object doCreateInstance() throws Exception {
        if (StringUtils.isBlank(this.jdbcUrl)) {
            UniversalLogger.warn("MysqlClientFactoryBean jdbcUrl is empty,so return null");
            return null;
        }
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClass(driverClass);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        dataSource.setCheckoutTimeout(checkoutTimeout);
        dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
        dataSource.setAcquireRetryDelay(acquireRetryDelay);
        dataSource.setMaxConnectionAge(maxConnectionAge);
        strategy.setTimeoutConfig(dataSource);
        UniversalLogger.info("MysqlClientFactoryBean create instance with jdbc url:"
                + dataSource.getJdbcUrl());
        return dataSource;
    }

    @Override
    public Class<ComboPooledDataSource> getObjectType() {
        return ComboPooledDataSource.class;
    }

    @Override
    protected void destroyInstance(ComboPooledDataSource instance) throws Exception {
        if (instance == null) {
            return;
        }
        // instance.close();
    }


}
