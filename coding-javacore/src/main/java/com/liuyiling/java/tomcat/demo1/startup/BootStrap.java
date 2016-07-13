package com.liuyiling.java.tomcat.demo1.startup;

import com.liuyiling.java.tomcat.demo1.connector.HttpConnector;

/**
 * Tomcat默认启动调用的主线程
 */
public class BootStrap {

    public static void main(String[] agrs){
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.start();
    }

}
