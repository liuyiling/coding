package com.liuyiling.java.tomcat.demo0;

/**
 * Created by liuyl on 16/7/13.
 */
public class StaticResourceProcessor {

    //静态资源的处理函数
    public void process(Request request, Response response){
        try{
            response.sendStaticResource();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
