import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by liuyl on 16/2/23.
 * log4j2的使用,其实非常简单，只要get到相应的日志记录系统,就可以输出了对应的日志到文件中
 *
 * 优先级：
 *  classpath下名为 log4j-test.json 或者log4j-test.jsn文件
    classpath下名为 log4j2-test.xml
    classpath下名为 log4j.json 或者log4j.jsn文件
    classpath下名为 log4j2.xml
 */
public class LogUseExample {

    //对应配置文件中的root,也就是console
    private static Logger logger = LogManager.getLogger();
    //对应配置文件中error,也就是errorFile
    private static Logger errorFileLogger = LogManager.getLogger("error");

    public static void main(String[] agrs){
        logger.debug("console debug");
        errorFileLogger.error("error file log");
    }


}
