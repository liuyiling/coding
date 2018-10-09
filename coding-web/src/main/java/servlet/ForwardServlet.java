package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by liuyl on 15/11/23.
 * forward用来从servlet-A跳转到servlet-B或者jsp-A
 * struts等MVC框架的底层转发工作原理其实就是不断地foward
 */
public class ForwardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String destination = req.getParameter("destination");

        switch (destination) {
            case "jsp":
                req.setAttribute("date", new Date());
                req.setAttribute("name", "Lewis Lau");
                req.getRequestDispatcher("/forward.jsp").forward(req, resp);
                break;
            case "html":
                req.getRequestDispatcher("/WEB-INF/forward.html").forward(req, resp);
                break;
            case "servlet":
                req.getRequestDispatcher("/servlet/LifeCycleServlet").forward(req, resp);
                break;
            case "resp-redirect":
                String contextPath = req.getContextPath();
                resp.sendRedirect(contextPath + "/servlet/LifeCycleServlet");
                break;
            case "resp-302":
                resp.setHeader("Location", "/servlet/LifeCycleServlet");
                resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                break;
            default:
                PrintWriter out = resp.getWriter();
                out.println("<html>");
                out.println("This is forward Servlet");
                out.println("</html>");
                out.flush();
                out.close();
        }
    }
}
