package servlet;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by liuyl on 15/11/22.
 * 参数Servlet,主要描述了request和resonse获取参数
 * 从web.xml中获取参数
 */

public class ParamServlet extends HttpServlet {

    //注入参数,需要在web.xml中配置entry-map
    private
    @Resource(name = "injectPrama")
    String injectPrama;

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
        System.out.println("Servlet的初始化参数: key=initParamOne" + " value=" + config.getInitParameter("initParamOne"));
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String value = config.getInitParameter(name);
            System.out.println("Servlet的初始化参数: key=" + name + " ,value=" + value);
        }

        ServletContext servletContext = getServletConfig().getServletContext();
        //在同一个webapps底下,多个servlet共用同一个context,是共享变量的一种途途径
        servletContext.setAttribute("key", "value");
        servletContext.getAttribute("key");

        //获取全局的参数
        String overallInitParameter = servletContext.getInitParameter("overallinitParam");
        System.out.println("ServletContect的全局参数: " + overallInitParameter);

        //获取get方法的参数
        String getMethodParameter = req.getParameter("queryParameter");
        String[] getMethodParameterValues = req.getParameterValues("queryParameter-values");
        String getMethodQueryString = req.getQueryString();

        System.out.println("getMethodParameter: " + getMethodParameter);
        System.out.println("getMethodQueryString: " + getMethodQueryString);

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
        String var1 = req.getParameter("var1");
        System.out.println("从post请求获得参数: key = var1" + var1);

        String var2 = req.getParameter("var2");
        System.out.println("从post请求获得参数: key = var2" + var2);

        return;
    }


}
