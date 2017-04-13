package org.liuyiling.chapter2.util;

/**
 * Created by liuyl on 2016/11/8.
 * 转型操作工具类
 */
public final class CastUtil {

    /**
     * 转换成String类型
     * @param object
     * @return
     */
    public static String castString(Object object){
        return CastUtil.castString(object, "");
    }

    public static String castString(Object object, String defaultValue) {
        return object != null ? String.valueOf(object) : defaultValue;
    }

    public static Double castDouble(Object object){
        return CastUtil.castDouble(object, 0);
    }

    public static Double castDouble(Object object, double defaultValue) {
        double doubleValue = defaultValue;
        if(object != null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    doubleValue = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    doubleValue = defaultValue;
                }
            }
        }

        return doubleValue;
    }

    public static Long castLong(Object object){
        return CastUtil.castLong(object, 0);
    }

    public static Long castLong(Object object, long defaultValue) {
        long longValue = defaultValue;
        if(object != null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    longValue = Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }

        return longValue;
    }

    public static Integer castInt(Object object){
        return CastUtil.castInt(object, 0);
    }

    public static Integer castInt(Object object, int defaultValue) {
        int intValue = defaultValue;
        if(object != null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }

        return intValue;
    }

    public static Boolean castBoolean(Object object){
        return CastUtil.castBoolean(object, false);
    }

    public static Boolean castBoolean(Object object, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if(object != null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    booleanValue = Boolean.parseBoolean(strValue);
                }catch (NumberFormatException e){
                    booleanValue = defaultValue;
                }
            }
        }

        return booleanValue;
    }


}
