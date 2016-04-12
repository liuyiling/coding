import javax.servlet.*;
import java.io.IOException;

/**
 * Created by liuyl on 15/11/26.
 */
public class CharacterEncodingFilter implements Filter {

    private String characterEncoding;
    private boolean enabled;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CharacterEncodingFilter 开始");
        characterEncoding = filterConfig.getInitParameter("characterEncoding");
        enabled = "true".equals(filterConfig.getInitParameter("enabled").trim());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("CharacterEncodingFilter 执行");
        if (enabled || characterEncoding != null) {
            servletRequest.setCharacterEncoding("UTF-8");
            servletResponse.setCharacterEncoding("UTF-8");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        System.out.println("CharacterEncodingFilter 执行完毕");
    }

    @Override
    public void destroy() {
        System.out.println("CharacterEncodingFilter 结束");
        characterEncoding = null;
    }
}
