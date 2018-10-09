package servlet;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static java.lang.System.out;

/**
 * Created by liuyl on 15/11/22.
 * 记录Servlet获取参数的方法,主要涉及：
 * 1.request和response获取参数
 * 2.从web.xml中获取参数
 */

public class ParamServlet extends HttpServlet {

    //注入参数,需要在web.xml中配置entry-map
    @Resource(name = "injectParam")
    private String injectParam;

    private ServletConfig config;

    @Override
    public void init() throws ServletException {
        this.config = getServletConfig();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        //每次执行的时候都加载初始化参数，会造成效率低下,解决的办法是写在init方法中,见LifeCycleServlet
        //获取servlet中配置的参数(非全局参数)
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String value = config.getInitParameter(name);
            out.println("Servlet内部的初始化参数: key=" + name + " ,value=" + value);
        }

        ServletContext servletContext = getServletConfig().getServletContext();
        //在同一个webapps底下,多个servlet共用同一个context,是共享变量的一种途途径
        servletContext.setAttribute("context-key", "context-value");

        //获取全局的参数
        String overallInitParameter = servletContext.getInitParameter("overall-initParam");
        out.println("ServletContext的全局参数: " + overallInitParameter);

        //获取注入的参数
        out.println("ServletContext的注入参数: " + injectParam);

        //获取get方法的参数
        String getMethodParameter = req.getParameter("queryParameter");
        String getMethodQueryString = req.getQueryString();

        out.println("getMethodParameter: " + getMethodParameter);
        out.println("getMethodQueryString: " + getMethodQueryString);

        resp.setContentType("text/html");

        //字符输出流,而不是字符输出流
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("This is InitParamServlet");
        out.println("</html>");

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取Post传来的参数
        String value1 = req.getParameter("var1");
        out.println("从post请求获得参数: key = " + value1);

        String value2 = req.getParameter("var2");
        out.println("从post请求获得参数: key = " + value2);

        //打印servletContext中共用的参数
        ServletContext servletContext = getServletConfig().getServletContext();
        String attribute = (String) servletContext.getAttribute("context-key");
        out.println("servletContext中共用的参数: " + attribute);
    }


}
