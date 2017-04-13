package org.liuyiling.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuyl on 2016/11/8.
 * 对apache的String工具类再做一次封装
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }

        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
