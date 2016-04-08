import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuyl on 15/11/26.
 */
public class LogFilter implements Filter {

    //记录日志的实际处理类
    private Log log = LogFactory.getLog(this.getClass());
    //filter的名称
    private String filterName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterName = filterConfig.getFilterName();
        log.info("启动 LogFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        log.info("LogFilter 开始处理");
        long startTime = System.currentTimeMillis();
        String requestUri = req.getRequestURI();

        filterChain.doFilter(req,res);

        long endTime = System.currentTimeMillis();
        log.info(servletRequest.getRemoteAddr() + "访问了： " + requestUri + " 用时：" + (endTime-startTime) );


        log.info("LogFilter 处理完毕");
    }

    @Override
    public void destroy() {
        log.info("LogFilter 关闭");
    }
}
