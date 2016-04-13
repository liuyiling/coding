import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liuyl on 15/12/5.
 * 第一个servlet的简单配置,用来上手
 * 包含了输出字符(text/html)和(image/jpeg)
 */
public class FirstServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie[] cookies = req.getCookies();
        Cookie cookie = new Cookie("key", "value");
        cookie.setMaxAge(Integer.MAX_VALUE);
        resp.addCookie(cookie);

        //修改cookie,生成一个同名的cookie来覆盖到原来的cookie
        //删除cookie
        cookie.setMaxAge(0);
        //使用SSL来操作cookie
        cookie.setSecure(true);

        //操作session
        HttpSession session = req.getSession(true);
        session.setAttribute("key", "value");
        //叫做JSESSIONID的cookie用来协同Session工作
        String id = session.getId();

        //如果client禁用了cookie，那么server在使用cookie和session的时候会遇到苦难，解决方案是url重写
        resp.encodeURL("index.jsp?c=1&wd=Java");

        this.excute(req, resp);
    }

    private void excute(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //设置编码方式
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        //获取请求报文中的数据
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        String param = req.getParameter("param");
        String userAgent = req.getHeader("User-Agent");
        String localAddr = req.getLocalAddr();

        System.out.println("requestURI : " + requestURI);
        System.out.println("method : " + method);
        System.out.println("param : " + param);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("localAddr: " + localAddr);


        //设置响应报文的格式
        resp.setContentType("text/html");

        //只能书写字符型的数据，是对outputStream的封装
        PrintWriter out = resp.getWriter();

        /*
        以下是输出二进制图片的代码
         */
        //书写二进制数据
        //resp.setContentType("image/jpeg");
        //BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        //Graphics2D g = bi.createGraphics();
        //Color color = new Color(255,255,255);
        //g.setColor(color);
        //
        //ServletOutputStream outImage = resp.getOutputStream();
        //
        //
        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outImage);
        //encoder.encode(bi);
        //
        //outImage.flush();
        //outImage.close();


        out.println("<html>");
        out.println("<body>");
        out.println("This is my first Servlet");
        out.println("</body>");
        out.println("</html>");

        out.flush();
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("execute doPost!");
        this.excute(req, resp);
    }
}
