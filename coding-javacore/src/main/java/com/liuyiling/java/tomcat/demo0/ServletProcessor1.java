package com.liuyiling.java.tomcat.demo0;


import javax.servlet.Servlet;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by liuyl on 16/7/13.
 */
public class ServletProcessor1 {

    public void process(Request request, Response response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try{
            URL[] urls = new URL[1];

            //找到所需Servlet类的位置存入URL
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);

            String repository = new URL("file", null, classPath.getCanonicalPath() + File.separator).toString();

            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (Exception e){

        }

        Class myClass = null;
        try{
            myClass = loader.loadClass("com.liuyiling.java.tomcat.demo0.PrimitiveServlet");
        }catch (Exception e){
        }

        Servlet servlet = null;

        try{
            //初始化servlet
            servlet = (Servlet)myClass.newInstance();
            servlet.service(request, response);
        }catch (Exception e){

        }
    }

}
