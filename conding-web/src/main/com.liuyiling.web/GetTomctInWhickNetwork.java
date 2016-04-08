import javax.servlet.*;
import java.io.IOException;

/**
 * Created by liuyl on 15/12/25.
 */
public class GetTomctInWhickNetwork implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ServletContext ctx = filterConfig.getServletContext();

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
