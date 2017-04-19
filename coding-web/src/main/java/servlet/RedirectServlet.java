package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liuyl on 15/12/6.
 * response的重定向
 */
public class RedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /**
         * 设置数据合理的缓存时间值，以避免浏览器频繁向服务器发送请求，提升服务器的性能
         * 这里是将数据的缓存时间设置为1天
         */
        resp.setDateHeader("expires", System.currentTimeMillis() + 24 * 3600 * 1000);
        String destination = req.getParameter("destination");

        if ("servlet.FirstServlet".equals(destination)) {
            System.out.println(req.getContextPath());
            resp.sendRedirect(req.getContextPath() + destination);
        } else {

            PrintWriter out = resp.getWriter();

            out.println("<html>");
            out.println("This is redirectServlet Servlet");
            out.println("</html>");

            out.flush();
            out.close();
        }
    }
}
