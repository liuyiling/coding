import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liuyl on 15/11/23.
 * servlet的生命周期
 */
public class LifeCycleServlet extends HttpServlet {

    private  static double startPoint = 0;

    /**
     * 尽量不要在Servlet中声明成员变量，否则会引来安全性问题
     * 因为tomcat为每个请求开辟一个新的线程，共用了同一份servlet代码，会有线程安全的问题
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        System.out.println("servlet init()");
        ServletConfig servletConfig = getServletConfig();
        startPoint = Double.parseDouble(servletConfig.getInitParameter("startPointer"));
    }

    /**
     * 所有的请求都会先经过service方法,然后才会分配到具体的doGet或者doPost方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet service()");
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet doGet()");

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("LifeCycleServlet");
        out.println("</html>");

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("servlet destroy()");
    }
}
