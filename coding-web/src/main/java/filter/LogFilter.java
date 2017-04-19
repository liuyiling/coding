package filter;

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

    //filter的名称
    private String filterName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterName = filterConfig.getFilterName();
        System.out.println("filter.LogFilter 初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        System.out.println("filter.LogFilter 执行");
        long startTime = System.currentTimeMillis();
        String requestUri = req.getRequestURI();

        filterChain.doFilter(req,res);

        long endTime = System.currentTimeMillis();
        System.out.println(servletRequest.getRemoteAddr() + "访问了： " + requestUri + " 用时：" + (endTime - startTime));


        System.out.println("filter.LogFilter 结束");
    }

    @Override
    public void destroy() {
        System.out.println("filter.LogFilter 销毁");
    }
}
