package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.GZIPOutputStream;

/**
 * Created by liuyl on 2017/4/17.
 * 这个Servlet讲述了如何使用Servlet去控制HTTP报文的头
 * <p>
 * 在开发过程中，如果希望服务器输出什么浏览器就能看到什么，那么在服务器端都要以字符串的形式进行输出。
 * PrintWriter流处理字节数据，会导致数据丢失，这一点千万要注意，因此在编写下载文件功能时，要使用OutputStream流，避免使用PrintWriter流，因为OutputStream流是字节流，可以处理任意类型的数据，而PrintWriter流是字符流，只能处理字符数据，如果用字符流处理字节数据，会导致数据丢失。
 */
public class HttpHeaderUseByServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String displayName = req.getParameter("display-name");

        try {
            switch (displayName) {
                case "outPut-chinese":
                    outputChinese(resp);
                    break;
                case "printWriter-chinese":
                    printWriterChinese(resp);
                    break;
                case "printWriter-numberic":
                    printWriterNumberic(resp);
                    break;
                case "download-file":
                    downloadFile(resp);
                    break;
                case "zip-data":
                    zipData(resp);
                    break;
                case "send-image":
                    sendImage(resp);
                    break;
                case "auot-refresh":
                    resp.setHeader("refresh", "3;url='http://www.baidu.com'");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printWriterNumberic(HttpServletResponse resp) throws Exception {
        resp.setHeader("content-type", "text/html;charset=UTF-8");
        OutputStream outputStream = resp.getOutputStream();
        outputStream.write("使用OutputStream流输出数字1：".getBytes("UTF-8"));
        outputStream.write((1 + "").getBytes());
    }

    private void outputChinese(HttpServletResponse resp) throws Exception {
        //通过控制响应头来告诉浏览器使用UTF-8编码进行显示
        String data = "中国";
        OutputStream outputStream = resp.getOutputStream();
        resp.setHeader("content-type", "text/html;charset=UTF-8");
        byte[] dataByteArr = data.getBytes("UTF-8");
        outputStream.write(dataByteArr);
    }

    private void printWriterChinese(HttpServletResponse resp) throws Exception {
        //另外一种发送中文数据的方式
        String data = "中国";
        resp.setHeader("content-type", "text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");//设置按照UTF-8的编码输出到客户端浏览器
        PrintWriter writer = resp.getWriter();//获取PrintWriter输出流
        writer.write(data);
    }

    private void zipData(HttpServletResponse resp) throws IOException {
        String data = "xxxxxxxx";
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(bout);
        gout.write(data.getBytes());
        gout.close();

        byte g[] = bout.toByteArray();
        resp.setHeader("Content-Encoding", "gzip");
        resp.setHeader("Content-Length", g.length + "");
        resp.getOutputStream().write(g);
    }

    private void sendImage(HttpServletResponse resp) throws IOException {
        String realPath = this.getServletContext().getRealPath("/img/image1.jpeg");

        InputStream in = new FileInputStream(realPath);
        int len = 0;
        byte buffer[] = new byte[1024];
        OutputStream out = resp.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        in.close();
    }

    private void downloadFile(HttpServletResponse resp) throws IOException {
        //下载文件
        String realPath = this.getServletContext().getRealPath("/img/张家界.jpeg");
        //注意这边的路径名,Mac和Windows的路径名间隔符号不一样
        String fileName = realPath.substring(realPath.lastIndexOf("\\") + 1);
        resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        InputStream in = new FileInputStream(realPath);
        int len = 0;
        byte buffer[] = new byte[1024];
        OutputStream out = resp.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        in.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
