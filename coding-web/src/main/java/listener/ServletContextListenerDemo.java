package listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyiling on 2018/2/13
 */
public class ServletContextListenerDemo implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, String> countries = new HashMap<>();
        countries.put("ca", "Canada");
        countries.put("us", "United States");
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("countries", countries);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
