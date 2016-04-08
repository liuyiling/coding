import javax.servlet.*;
import java.io.IOException;

/**
 * Created by liuyl on 15/11/26.
 */
public class MyFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter 开始");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter 处理请求");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("MyFilter 处理请求结束");
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter 结束");
    }
}
