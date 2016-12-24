package com.liuyiling.common.util;

import com.liuyiling.common.thread.RequestTraceContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 程序通用的logger记录文件，
 * 关于logger具体的使用方式可以查看coding-log4j这个module
 * Created by liuyl on 2016/12/20.
 */
public class UniversalLogger {

    //MC超时时间
    public static long MC_FIRE_TIME = 500L;
    //Database超时时间
    public static long DB_FIRE_TIME = 1000L;
    //Redis超时时间
    public static long REDIS_FIRE_TIME = 500L;

    private static Logger api = LoggerFactory.getLogger("api");
    private static Logger infoLog = LoggerFactory.getLogger("info");
    private static Logger warnLog = LoggerFactory.getLogger("warn");
    private static Logger errorLog = LoggerFactory.getLogger("error");
    private static Logger queueFailoverLog = LoggerFactory.getLogger("queuefailover");
    private static Logger testLog = LoggerFactory.getLogger("testlog");
    private static Logger redoLog = LoggerFactory.getLogger("redoLog");
    private static Logger fireLog = LoggerFactory.getLogger("fire");
    private static Logger scribeLog = LoggerFactory.getLogger("scribe");
    private static Logger exposureLog = LoggerFactory.getLogger("exposure");
    private static Logger monitorLog = LoggerFactory.getLogger("monitor");

    public UniversalLogger() {
    }

    public static boolean isTraceEnabled() {
        return api.isTraceEnabled();
    }

    public static boolean isDebugEnabled() {
        return api.isDebugEnabled();
    }

    public static void trace(Object msg) {
        if(msg != null && api.isTraceEnabled()) {
            api.trace(msg.toString());
        }

    }

    public static void trace(String msg, Object... args) {
        if(api.isTraceEnabled()) {
            api.trace(msg, args);
        }

    }

    public static void debug(Object msg) {
        if(msg != null && api.isDebugEnabled()) {
            String msg1 = assembleRequestId(msg.toString());
            api.debug(msg1.toString());
        }

    }

    public static void debug(String msg, Object... args) {
        if(api.isDebugEnabled()) {
            msg = assembleRequestId(msg);
            api.debug(msg, args);
        }

    }

    public static void fire(String msg, Object... args) {
        if(fireLog.isInfoEnabled()) {
            fireLog.info(msg, args);
        }

    }

    public static void exposure(String msg, Object... args) {
        exposureLog.info(msg, args);
    }

    public static void monitor(String msg, Object... args) {
        monitorLog.info(msg, args);
    }

    public static void logRedo(String msg, Throwable e) {
        redoLog.warn(msg, e);
    }

    public static void scribe(String msg) {
        if(scribeLog.isDebugEnabled()) {
            scribeLog.debug(msg);
        }

    }

    public static void info(Object msg) {
        if(msg != null && infoLog.isInfoEnabled()) {
            String msg1 = assembleRequestId(msg.toString());
            infoLog.info(msg1.toString());
        }

    }

    public static void info(String msg, Object... args) {
        if(infoLog.isInfoEnabled()) {
            msg = assembleRequestId(msg);
            infoLog.info(msg, args);
        }

    }

    public static void queueFailover(String msg) {
        if(queueFailoverLog.isInfoEnabled()) {
            queueFailoverLog.info(msg);
        }

    }

    public static void warn(Object msg) {
        if(msg != null) {
            String log = assembleRequestId(msg.toString());
            warnLog.warn(log);
        }

    }

    public static void warn(String msg, Object... args) {
        msg = assembleRequestId(msg);
        warnLog.warn(msg, args);
    }

    public static void warn(String msg, Throwable e) {
        msg = assembleRequestId(msg);
        warnLog.warn(msg, e);
    }

    public static void error(Object msg) {
        if(msg != null) {
            String log = assembleRequestId(msg.toString());
            errorLog.error(log);
        }

    }

    public static void error(String msg, Object... args) {
        msg = assembleRequestId(msg);
        errorLog.error(msg, args);
    }

    public static void error(String msg, Throwable e) {
        msg = assembleRequestId(msg);
        errorLog.error(msg, e);
    }

    public static void logForTest(String msg) {
        testLog.info(msg);
    }

    public static void logForTest(String msg, Throwable e) {
        testLog.info(msg, e);
    }

    public static String assembleRequestId(String msg) {
        if(msg != null) {
            msg = assembleRequestId(msg, " ");
        } else {
            msg = assembleRequestId("null", " ");
        }

        return msg;
    }

    private static String assembleRequestId(String msg, String spit) {
        String requestId = getRequestId();
        StringBuilder buf = new StringBuilder(msg);
        if(!StringUtils.isEmpty(requestId)) {
            buf.append(spit).append(requestId);
            msg = buf.toString();
        }

        return msg;
    }

    public static String getRequestId() {
        RequestTraceContext context = RequestTraceContext.get();
        String requestId = "";
        if(context != null) {
            requestId = "r=" + context.getId();
        }

        return requestId;
    }

}
