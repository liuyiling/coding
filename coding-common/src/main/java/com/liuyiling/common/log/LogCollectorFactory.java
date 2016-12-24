package com.liuyiling.common.log;

/**
 * Created by liuyl on 2016/12/20.
 */
public class LogCollectorFactory {
    private static final ScribeLogCollector scribeLog = new ScribeLogCollector();

    public LogCollectorFactory() {
    }

    public static LogCollector getLogCollector() {
        return scribeLog;
    }

}
