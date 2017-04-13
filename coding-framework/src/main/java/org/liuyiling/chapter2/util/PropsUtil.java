package org.liuyiling.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by liuyl on 2016/11/8.
 * 属性文件工具类
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 根据制定的文件名加载定制的配置文件
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;

        try {
            //开发的classpath是src/java或src/resources.测试的classpath是test/java或test/resources.
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + "file not found");
            }

            props = new Properties();
            props.load(is);
        } catch (Exception e) {
            LOGGER.error("load properties file failure", e);
        } finally {
            //及时关闭输入输出流
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    LOGGER.error("close inputStream failure", e);
                }
            }
        }

        return props;
    }

    /**
     * 获取String类型的配置
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    public static String getString(Properties props, String key, String defaultValue) {
        String value = defaultValue;

        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }

        return value;
    }


    /**
     * 获取int类型的配置
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    public static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;

        if (props.containsKey(key)) {
            value = CastUtil.castInt(props.getProperty(key));
        }

        return value;
    }


    /**
     * 获取boolean类型的配置
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }

    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean value = defaultValue;

        if (props.containsKey(key)) {
            value = CastUtil.castBoolean(props.getProperty(key));
        }

        return value;
    }
}
