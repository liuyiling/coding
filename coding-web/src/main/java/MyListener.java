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

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        long time = System.currentTimeMillis() - (long)request.getAttribute("dateCreated");
        log.info(request.getRemoteAddr()+ "请求结束：用时：" + time);
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {

        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        String uri = request.getRequestURI();

        request.setAttribute("dateCreated",System.currentTimeMillis());
        log.info("MyListener: IP: " + request.getRemoteAddr() + "请求：" +uri);
    }


}
