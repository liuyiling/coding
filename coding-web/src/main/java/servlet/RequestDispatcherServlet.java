package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuyl on 15/12/5.
 * web-inf目录下是不可直接Url访问的，但是可以用程序转发访问
 */
public class RequestDispatcherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //设置编码方式
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        //转发的两种方式
        this.getServletContext().getRequestDispatcher("/WEB-INF/invisibleHtml.html").forward(req, resp);
//        req.getRequestDispatcher("/WEB-INF/invisibleHtml.html").forward(req,resp);
    }
}
