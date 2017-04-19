package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuyl on 2017/4/18.
 */
public class CheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientCheckCode = req.getParameter("validateCode");
        String serverCheckCode = (String) req.getSession().getAttribute("checkcode");

        if(clientCheckCode.equals(serverCheckCode)){
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
