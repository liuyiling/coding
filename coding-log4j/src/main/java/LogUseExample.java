import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

/**
 * Created by liuyl on 16/2/23.
 * log4j2的使用,其实非常简单,只要获得Logger对象,就可以使用该Logger对象输出日志到各种介质中（文件、控制台）等
 *
 * log4j主要由三部分组成：这三部分都可以在配置文件中用xml格式的文件配置（也是最普遍的做法）
 * logger:日志采集者
 * appenders:日志输出者
 * layouts:日志输出格式管理者
 *
 * 日志有多种级别,分别是:ALL,DEBUG,ERROR,FATAL,INFO,OFF,TRACE,WARN。每个级别的具体作用百度上都有
 * 级别的顺序为：ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF。在一个级别为q的日志对象中，只能输出级别>=q的日志
 *
 * 优先级：
 *  log4j2-test.json 或log4j2-test.jsn文件 　
    log4j2-test.xml文件 　　
    log4j2.json 或log4j2.jsn文件 　　
    log4j2.xml文件
 *
 * log4j2 与 slf4j的集成关系
 */
public class LogUseExample {

    //到配置文件中获取不到声明的logger,就获取rootLogger
    public static Logger normalLogger = LoggerFactory.getLogger(LogUseExample.class);
    //到配置文件中获取得到声明的errorLogger
    public static Logger errorLogger = LoggerFactory.getLogger("error");

    public static void main(String[] agrs){
        //rootLogger的默认级别为debug,这两行日志都应该输出
        normalLogger.info("normalLogger info log");
        normalLogger.error("normalLogger error log");

        //errorLogger的默认级别为error,info日志不应该输出
        errorLogger.info("errorLogger info log");
        errorLogger.error("errorLogger error log");
    }


}
