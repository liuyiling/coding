package filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by liuyl on 15/11/26.
 * Filter的使用方式示范
 */
public class MyFilter implements Filter{

    //web程序启动时调用这个方法
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter.MyFilter 初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter.MyFilter 开始");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("filter.MyFilter 结束");
    }

    @Override
    public void destroy() {
        System.out.println("filter.MyFilter 销毁");
    }
}
