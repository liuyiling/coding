package com.liuyiling.java.tomcat.demo1;

import com.liuyiling.java.tomcat.demo1.connector.HttpRequest;
import com.liuyiling.java.tomcat.demo1.connector.HttpRequestFacade;
import com.liuyiling.java.tomcat.demo1.connector.HttpResponse;
import com.liuyiling.java.tomcat.demo1.connector.HttpResponseFacade;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by liuyl on 16/6/27.
 */
public class ServletProcessor {


    public void process(HttpRequest request, HttpResponse response){

        String uri = request.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try{
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File("webroot");
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);

        } catch (Exception e) {
        }

        Class myClass = null;
        try{
            myClass = loader.loadClass(servletName);
        } catch (Exception e){
        }

        Servlet servlet = null;

        try{
            servlet = (Servlet)myClass.newInstance();
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);

            servlet.service(requestFacade, responseFacade);
            response.finishResponse();

        } catch (Exception e){

        }


    }

}
