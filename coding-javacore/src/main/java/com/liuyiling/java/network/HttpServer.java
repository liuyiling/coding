package com.liuyiling.java.network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 模拟一个小的tomcat服务器
 */
public class HTTPServer{

    //服务器启动目录
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    //服务器关闭命令
    public static final String SHUTDOWN = "SHUTDOWN";
    public static boolean shutdown = false;

    //服务器轮询
    public static void main(String[] agrs) throws IOException {
        HTTPServer httpServer = new HTTPServer();
        await();
    }

    public static void await() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080, 20, InetAddress.getByName("localhost"));

        while(!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;

            try{
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                Request request = new Request(input);
                request.parse();

                Response response = new Response(request, output);
                response.sendStaticResource();

                socket.close();
                shutdown = request.getUri().equals(SHUTDOWN);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }

    }

}


class Request{

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public void parse(){
        StringBuffer request = new StringBuffer(1024);
        byte[] buffer = new byte[1024];

        int i;
        try {
            //InputStream一次读取整个字符缓冲区
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        //此时HTTP请求报文在buffer数组中
        for(int j = 0; j < i;j++){
            request.append((char)buffer[j]);
        }

        System.out.println(request.toString());
        uri = parseUri(request.toString());
    }

    private String parseUri(String request){
        //解析URI的代码，暂时不写
        return "index.html";
    }

    public String getUri() {
        return uri;
    }
}

class Response{

    private Request request;
    private OutputStream out;

    public Response(Request request, OutputStream out) {
        this.request = request;
        this.out = out;
    }

    public void sendStaticResource() throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;

        //如果请求的文件存在,则开始回写
        try{
            File file = new File(HTTPServer.WEB_ROOT, request.getUri());
            if(file.exists()){
                fis = new FileInputStream(file);
                int ch = fis.read(buffer, 0,1024);

                //未结束
                while(ch != -1){
                    out.write(buffer, 0, 1024);
                    ch = fis.read(buffer, 0,1024);
                }
            }else {
                String errorMeesgae = "HTTP/1.1 404 Not Found\r\n";
                out.write(errorMeesgae.getBytes());
            }
        } catch (Exception e){
            System.out.println(e.toString());
        } finally {
            if(fis != null){
                fis.close();
            }
        }
    }

}