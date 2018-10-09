import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/path2/cookie-demo")
public class CookieDemo2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int len = -1;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			len = cookies.length;
		}
		response.getWriter().write(String.valueOf(len));
	}

}
