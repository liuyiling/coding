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
public class RedirectServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String destination = req.getParameter("destination");

        if("FirstServlet".equals(destination)){
            System.out.println(req.getContextPath());
            resp.sendRedirect(req.getContextPath() + destination);
        }
        else {

            PrintWriter out = resp.getWriter();

            out.println("<html>");
            out.println("This is redirectServlet Servlet");
            out.println("</html>");

            out.flush();
            out.close();
        }
    }
}
