package connection.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyl on 16/4/4.
 * 数据库连接池
 * 传统的数据库连接方式想必大家都记得：Connetcion con = DriverManager.getConnection(url)
 * 当我们创建了一个Connection对象，它在内部都执行了什么：
 * 1.“DriverManager”检查并注册驱动程序，
 * 2.“com.mysql.jdbc.Driver”就是我们注册了的驱动程序，它会在驱动程序类中调用“connect(url…)”方法。
 * 3.com.mysql.jdbc.Driver的connect方法根据我们请求的“connUrl”，创建一个“Socket连接”，连接到IP为“your.database.domain”，默认端口3306的数据库。
 * 4.创建的Socket连接将被用来查询我们指定的数据库，并最终让程序返回得到一个结果。
 * <p>
 * 如果建立socket连接的时间比执行sql的时间还长，那么这个连接就是低效率的，所以我们必须保持长连接，就如HTTP需要保持长连接一个道理
 * <p>
 * <p>
 * 数据库连接池的原理是：
 * 生成几个数据库的连接，把这些连接放在可用的集合中，由应用程序动态地对池中的连接进行申请、使用和释放。
 * 对于多于连接池中连接数的并发请求，应该在请求队列中排队等待。并且应用程序可以根据池中连接的使用率，动态增加或减少池中的连接数。
 */
public class DataBaseConnectionPool {


    private int poolSize;

    /**
     * 创建连接池的初始化大小
     */
    String connUrl = "jdbc:xx";
    String driver = "com.mysql.jdbc.Driver";
    private Map<Connection, String> connectionPool = null;

    private void initPool() {
        try {
            connectionPool = new HashMap<Connection, String>();
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connUrl);
            for (int i = poolSize; i < 0; i--) {
                connectionPool.put(con, "AVAILABLE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {

        boolean isConnectionAvailable = true;
        for (Map.Entry<Connection, String> entry : connectionPool.entrySet()) {
            synchronized (entry) {
                if (entry.getValue() == "AVAILABLE") {
                    entry.setValue("NOTAVAILABLE");
                    return entry.getKey();
                }
                isConnectionAvailable = false;
            }
        }

        /**
         * 自动扩容
         */
        if (!isConnectionAvailable) {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connUrl);
            connectionPool.put(con, "NOTAVAILABLE");
            return con;
        }

        return null;


    }


    /**
     * 关闭连接
     */
    public void closeConnection(Connection connection) {
        for (Map.Entry<Connection, String> entry : connectionPool.entrySet()) {
            synchronized (entry) {
                if (entry.getKey().equals(connection)) {
                    entry.setValue("AVAILABLE");
                }
            }
        }
    }
}