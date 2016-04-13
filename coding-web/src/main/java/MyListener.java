import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuyl on 15/11/26.
 * 监听器的使用案例
 */
public class MyListener implements ServletRequestListener {


    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {

        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        String uri = request.getRequestURI();

        request.setAttribute("dateCreated",System.currentTimeMillis());
        System.out.println("MyListener: IP: " + request.getRemoteAddr() + "请求：" + uri);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        long time = System.currentTimeMillis() - (long)request.getAttribute("dateCreated");
        System.out.println(request.getRemoteAddr()+ "请求结束：用时：" + time);
    }




}
