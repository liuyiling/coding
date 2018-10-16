package com.liuyiling.java.conrruent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by liuyl on 15/11/29.
 * ThreadLocal的解析
 */
public class ThreadLocalTest {

    //按照这种方法管理数据库连接，不会产生线程冲突问题，但是会消耗内存
    public void insert() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager();
        Connection connection = connectionManager.getConnection();
        //insert的业务逻辑
        connection.close();
    }
}


class ConnectionManager {
    //按照这种方法管理数据库连接的话，会产生线程冲突问题
    private Connection connection = null;

    public Connection getConnection() throws SQLException {
        //这里可能会产生同步问题，如果有两个线程A，B同时进入到这一行代码，发现connection都为null
        //线程A和线程B就会重复创建Connection
        if (connection == null) {
            connection = DriverManager.getConnection("xx");
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        //如果线程A在使用connection进行访问，线程2检测到connection!=null，进行关闭
        //这也是一种错误，应该对connection进行互斥访问
        if (connection != null) {
            connection.close();
        }
    }
}