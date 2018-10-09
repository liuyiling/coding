package servlet;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by liuyl on 15/12/5.
 * 第一个servlet的简单配置,用来上手
 * 包含了输出字符(text/html)和(image/jpeg)
 */
public class HtmlAndImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        excute(req, resp, 1);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("execute doPost!");
        this.excute(req, resp, 0);
    }

    private void excute(HttpServletRequest req, HttpServletResponse resp, int type) throws IOException {

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


        if (type == 0) {
            PrintWriter out = resp.getWriter();
            out.println("<html>");
            out.println("<body>");
            out.println("This is my first Servlet");
            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();
        } else {
            resp.setContentType("image/jpeg");
            BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            Color color = new Color(255, 255, 255);
            g.setColor(color);
            ServletOutputStream outImage = resp.getOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outImage);
            encoder.encode(bi);
            outImage.flush();
            outImage.close();
        }
    }
}
