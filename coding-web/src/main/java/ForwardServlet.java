import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by liuyl on 15/11/23.
 * forward用来跳转servlet到servlet或者jsp
 * 例如 MVC框架中，M->V->C
 */
public class ForwardServlet extends HttpServlet{


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String destination = req.getParameter("destination");

        if("jsp".equals(destination)){
            //struts等MVC框架的原理
            req.setAttribute("date",new Date());

            req.getRequestDispatcher("/forward.jsp").forward(req,resp);

        } else if("html".equals(destination)){
            req.getRequestDispatcher("/WEB-INF/forward.html").forward(req, resp);
        } else  if("servlet".equals(destination)){
            req.getRequestDispatcher("/servlet/LifeCycleServlet").forward(req,resp);
        }
        else {

            PrintWriter out = resp.getWriter();

            out.println("<html>");
            out.println("This is forward Servlet");
            out.println("</html>");

            out.flush();
            out.close();

        }

    }
}
