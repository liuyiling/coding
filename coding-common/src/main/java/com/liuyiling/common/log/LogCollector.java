package com.liuyiling.common.log;

/**
 *
 * Created by liuyl on 2016/12/20.
 */
public interface LogCollector {

    void log(String var1, String var2, String var3, LogCollector.DType var4, Object var5);

    void log(String var1, String var2, String var3);

    enum DType {
        RAWSTR(0),
        JSON(1);

        private int value;
        DType(int value) {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }


}
