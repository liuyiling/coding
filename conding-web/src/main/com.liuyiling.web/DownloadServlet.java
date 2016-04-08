import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyl on 15/11/23.
 */
public class DownloadServlet extends HttpServlet {

    Map<String,Integer> map = new HashMap<>();

    @Override
    public void init() throws ServletException {
        map.put("index.jsp",0);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("filename");
        if(filename != null){
            resp.sendRedirect("/index.jsp");
        } else {

            //自动刷新，单位是s
            resp.setHeader("Refresh", "1;URL=http://localhost:8080/servlet/ServletTest");

            PrintWriter out = resp.getWriter();

            out.println("<html>");
            out.println("This is Download Servlet");
            out.println("</html>");

            out.flush();
            out.close();



        }
    }
}
