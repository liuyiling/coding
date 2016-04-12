import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liuyl on 15/11/22.
 * 主要的servlet API 的练习
 */


public class ParamServlet extends HttpServlet {

    //注入参数,需要在web.xml中配置entry-map
    private @Resource(name = "injectPrama") String injectPrama;


    /**
     * 请求报文由服务器的tomcat进行封装成HttpServletRequest request
     * 处理get请求
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //每次执行的时候都加载初始化参数，会造成效率低下,解决的办法是写在init方法中,见LifeCycleServlet
        //获取servlet中配置的参数
        System.out.println("InitParameter: " + getInitParameter("initParamOne"));

        //获取全局的参数
        ServletContext servletContext = getServletConfig().getServletContext();
        String overallInitParameter = servletContext.getInitParameter("overallinitParam");
        System.out.println("overallInitParameter: " + overallInitParameter);

        //获取get方法的参数
        String getMethodParameter = req.getParameter("queryParameter");
        String getMethodQueryString = req.getQueryString();

        System.out.println("getMethodParameter: " + getMethodParameter);
        System.out.println("getMethodQueryString: " + getMethodQueryString);

        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("This is InitParamServlet");
        out.println("</html>");

        out.flush();
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        //每次执行的时候都加载初始化参数，会造成效率低下,解决的办法是写在init方法中,见LifeCycleServlet
        //获取servlet中配置的参数
        System.out.println("InitParameter: " + getInitParameter("initParamOne"));

        //获取全局的参数
        ServletContext servletContext = getServletConfig().getServletContext(); 
        String overallInitParameter = servletContext.getInitParameter("overallinitParam");
        System.out.println("overallInitParameter: " + overallInitParameter);


        //获取Post传来的参数
        String postVaribale1 = req.getParameter("postVaribale1");
        System.out.println("postVaribale1: " + postVaribale1);

        String postVaribale2 = req.getParameter("postVaribale2");
        System.out.println("postVaribale2: " + postVaribale2);

        //因为WEB-INF默认是不能访问的，所以可以做权限控制
        //
        req.getRequestDispatcher("/WEB-INF/invisibleHtml.html").forward(req, resp);
        return;
    }


}
