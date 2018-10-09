package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.out;

/**
 * Created by liuyiling on 2018/2/7
 */
@WebServlet(name = "CookieAndSessionServlet", urlPatterns = "/CookieAndSessionServlet")
public class CookieAndSessionServlet extends HttpServlet {

    public static int times = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        out.println("---------------开始输出已有cookie---------------");
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                out.println("已有cookie: key=" + cookie.getName() + " ,value=" + cookie.getValue());
            }
        }
        out.println("---------------结束输出已有cookie---------------");

        times++;
        Cookie cookie = new Cookie("liuyiling", "test");
        cookie.setDomain("localhost");
        cookie.setPath("/");

        if (times % 2 == 0) {
            cookie.setMaxAge(60 * 60 * 1);
            req.getSession().setAttribute("times", times);
        } else {
            cookie.setMaxAge(0);
        }
        resp.addCookie(cookie);

        out.println("---------------开始输出已有session---------------");
        out.println(req.getSession().getAttribute("times"));
        out.println("---------------结束输出已有session---------------");


        PrintWriter out = resp.getWriter();
        out.print("cookie test");
    }
}