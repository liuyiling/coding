package servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by liuyl on 2017/4/18.
 */
public class RandomImageServlet extends HttpServlet {

    private static final long serialVersionUID = 3038623696184546092L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BufferedImage image = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 20);
        g.setColor(Color.BLUE);
        g.setFont(new Font(null, Font.BOLD, 20));
        String randomNum = makeNum();
        g.drawString(randomNum, 0, 20);

        req.getSession().setAttribute("checkcode", randomNum);

        //设置浏览器禁止缓存当前文档内容
        resp.setContentType("image/jpeg");
        resp.setDateHeader("expries", -1);
        resp.setHeader("Cache-control", "no-cache");
        resp.setHeader("Pragma", "no-cache");

        ImageIO.write(image, "jpg", resp.getOutputStream());
    }

    private String makeNum() {
        Random random = new Random();
        String num = random.nextInt(9999) + "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 7 - num.length(); i++) {
            sb.append("0");
        }
        num = sb.toString() + num;
        return num;
    }


}
