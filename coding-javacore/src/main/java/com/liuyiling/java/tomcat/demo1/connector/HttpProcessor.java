package com.liuyiling.java.tomcat.demo1.connector;


import com.liuyiling.java.tomcat.demo1.ServletProcessor;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Connector连接器负责轮询等待HTPP连接，并且生成servletRequest 和 servletResponse 来交给HttpProcessor调用
 * 每个Http请求都会有一个socket和一个HttpProcessor实例来进行处理
 */
public class HttpProcessor {

    private HttpConnector connector = null;
    private HttpRequest request = null;
    private HttpResponse response = null;
    private HttpRequestLine requestLine = new HttpRequestLine();

    protected String method = null;
    protected String queryString = null;

    public HttpProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    /**
     * 每个Http请求的主要处理函数
     * @param socket
     */
    public void process(Socket socket){
        SocketInputStream input = null;
        OutputStream output= null;

        try{
            input = new SocketInputStream(socket.getInputStream(), 2048);
            output = socket.getOutputStream();

            request = new HttpRequest(input);
            response = new HttpResponse(output);

            response.setRequest(request);
            response.setHeader("Server", "Pyrmont Servlet Container");

            //解析请求行，不解析请求行之外的参数
            //请求行例子：GET /myApp/ModernServlet?userName=tarzan&password=pwd HTTP/1.1
            parseRequest(input, output);
            //解析头部
            parseHeaders(input);

            if(request.getRequestURI().startsWith("/servlet")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                //处理静态资源
            }


        }catch (Exception e){

        }
    }

    private void parseHeaders(SocketInputStream input) {

    }

    private void parseRequest(SocketInputStream input, OutputStream output) {

    }

}