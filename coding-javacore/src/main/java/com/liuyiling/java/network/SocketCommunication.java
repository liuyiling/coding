package com.liuyiling.java.network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by liuyl on 16/6/25.
 * 这一章构建一个小的tomcat容器
 */
public class SocketCommunication {

    //客户端发起一个HTTP请求都需要new一个Sokcet来与对应的服务器进行通信
    public static void createClientSokcet() throws IOException, InterruptedException {

        //建立一个从本机到服务器的sokcet
        Socket socket = new Socket("192.168.41.23", 80);

        //获取输入输出流
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //BufferReader一次读取一个字符
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("GET /index.jsp HTTP/1.1");
        out.println("Host: localhost:8080");
        out.println("Connection: close");
        out.println();

        boolean loop = true;

        StringBuffer sb = new StringBuffer(1024);
        while(loop){

            if(in.ready()){
                int i = 0;
                //结束符是-1
                while(i != -1){
                    i = in.read();
                    sb.append(i);
                }
                loop = false;
            }
            Thread.currentThread().sleep(50);
        }

        System.out.println(sb.toString());
        socket.close();
    }

}

