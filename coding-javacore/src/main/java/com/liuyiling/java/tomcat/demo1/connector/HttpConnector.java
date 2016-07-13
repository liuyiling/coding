package com.liuyiling.java.tomcat.demo1.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by liuyl on 16/6/27.
 * 更加细化的tomcat容器
 */
public class HttpConnector implements Runnable{

    boolean stopped;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }

    @Override
    public void run() {

        /**
         * 加载servlert，初始化数据库连接池等操作
         * 主要做了三件事：
         * 1.等待HTTP请求
         * 2.为每个请求创建HttpProcessor实例
         * 3.调用HttpProcessor处理一个请求
         */

        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!stopped){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                continue;
            }

            HttpProcessor httpProcessor = new HttpProcessor(this);
            httpProcessor.process(socket);
        }

    }

    public void start(){
        Thread thread = new Thread();
        thread.start();
    }

}