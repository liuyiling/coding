import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/path1/cookie-demo")
public class CookieDemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// cookie增删改查
		
		// 读取cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ", " 
													+ cookie.getValue() + ", " 
													+ cookie.getMaxAge() + ", " 
													+ cookie.getPath() + ", "
													+ cookie.getDomain() + ", "
													+ cookie.getSecure() + ", "
													+ cookie.isHttpOnly());
			}
		}		
		// 写cookie
		Cookie cookie = new Cookie("mycookie", String.valueOf(System.currentTimeMillis())); // name & value
		cookie.setHttpOnly(false); // JavaScript不能处理
		cookie.setMaxAge(-1000); // expires:24小时过期
		cookie.setSecure(false); // 如果为true，仅支持HTTPS协议
		cookie.setPath("/path2");
		// cookie.setDomain("www.test.loc");
		
		response.addCookie(cookie);
		
		// 删除cookie
		// cookie.setMaxAge(-1); // expires:一个过去的时间
	}

}
