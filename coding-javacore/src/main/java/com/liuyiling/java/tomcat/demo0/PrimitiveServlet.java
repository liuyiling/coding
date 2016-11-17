package com.liuyiling.java.tomcat.demo0;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liuyl on 16/6/25.
 * 最初始的servlet:
 * 包含了servlet的生命周期
 */
public class PrimitiveServlet implements Servlet{


    @Override
    public void init(ServletConfig config) throws ServletException {
        //servlet被初始化后,init会被容器调用：一般用来加载数据库等
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("from service");

        //输出HTTP的返回结果
        PrintWriter writer = res.getWriter();
        writer.println("Hello.I am 610");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}
