package com.liuyiling.common.util;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.CRC32;

/**
 * Created by liuyl on 2016/12/29.
 */
public class ApiUtil {

    private static Map<Byte, String> genders = new HashMap();
    private static final String GENDER_MALE = "m";
    private static final String GENDER_FEMALE = "f";
    private static final String GENDER_NONE = "n";
    private static Random random = new Random();
    public static final int MQ_PROCESS_ABORT = -1;
    public static final int MQ_PROCESS_RETRY = 0;
    public static final int MQ_PROCESS_DEGRADATION = -2;
    public static final int MQ_PROCESS_SUCCESS = 1;
    private static ThreadLocal<CRC32> crc32Provider = new ThreadLocal() {
        protected CRC32 initialValue() {
            return new CRC32();
        }
    };
    public static ThreadLocal<Boolean> hasVisible;
    private static final Map<String, Integer> lang2CodeMap;
    private static final Map<Integer, String> code2LangMap;

    public ApiUtil() {
    }

    public static String getGender(byte genderCode) {
        String gender = (String)genders.get(Byte.valueOf(genderCode));
        return gender != null?gender:"n";
    }

    public static long getCrc32(byte[] b) {
        CRC32 crc = (CRC32)crc32Provider.get();
        crc.reset();
        crc.update(b);
        return crc.getValue();
    }

    public static long getCrc32(String str) {
        try {
            return getCrc32(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException var2) {
            UniversalLogger.warn((new StringBuilder(64)).append("Error: getCrc32, str=").append(str).toString(), var2);
            return -1L;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Long[] toLongArr(String[] strArr) {
        return ArrayUtil.toLongArr(strArr);
    }

    /** @deprecated */
    @Deprecated
    public static Long[] toLongArr(long[] longArr) {
        return ArrayUtil.toLongArr(longArr);
    }

    /** @deprecated */
    @Deprecated
    public static long[] toRawLongArr(String[] strArr) {
        return ArrayUtil.toRawLongArr(strArr);
    }

    /** @deprecated */
    @Deprecated
    public static long[] toLongArr(Collection<Long> ids) {
        return ArrayUtil.toLongArr(ids);
    }

    /** @deprecated */
    @Deprecated
    public static String longArrToPrintableString(long[] longArr) {
        return ArrayUtil.longArrToPrintableString(longArr);
    }

    /** @deprecated */
    @Deprecated
    public static String stringArrToPrintableString(String[] objectArr) {
        return ArrayUtil.stringArrToPrintableString(objectArr);
    }

    /** @deprecated */
    @Deprecated
    public static String formatDate(Date date, String defaultValue) {
        return ApiDateUtil.formatDate(date, defaultValue);
    }

    /** @deprecated */
    @Deprecated
    public static Date parseDate(String dateStr, Date defaultValue) {
        return ApiDateUtil.parseDate(dateStr, defaultValue);
    }

    /** @deprecated */
    @Deprecated
    public static String getYearMonth(Date date) {
        return ApiDateUtil.getYearMonth(date);
    }

    public static String getYearMonthForSI(Date date) {
        return getYearMonth(date);
    }

    /** @deprecated */
    @Deprecated
    public static String formateDateTime(Date date) {
        return ApiDateUtil.formateDateTime(date);
    }

    /** @deprecated */
    @Deprecated
    public static Date parseDateTime(String timeStr, Date defaultValue) {
        return ApiDateUtil.parseDateTime(timeStr, defaultValue);
    }

    /** @deprecated */
    @Deprecated
    public static final int getCurrentHour() {
        return ApiDateUtil.getCurrentHour();
    }

    /** @deprecated */
    @Deprecated
    public static final int getLastHour() {
        return ApiDateUtil.getLastHour();
    }

    /** @deprecated */
    @Deprecated
    public static final int getNextHour() {
        return ApiDateUtil.getNextHour();
    }

    public static String getAttentionHash(long uid, int tblCount) {
        int hash = getHash4split(uid, tblCount);
        String hex = Long.toHexString((long)hash);
        if(hex.length() == 1) {
            hex = "0" + hex;
        }

        return hex;
    }

    public static int getHash4split(long id, int splitCount) {
        try {
            long e = getCrc32(String.valueOf(id).getBytes("utf-8"));
            if(e < 0L) {
                e = -1L * e;
            }

            int hash = (int)(e / (long)splitCount % (long)splitCount);
            return hash;
        } catch (UnsupportedEncodingException var6) {
            UniversalLogger.warn((new StringBuilder(64)).append("Error: when hash4split, id=").append(id).append(", splitCount=").append(splitCount).toString(), var6);
            return -1;
        }
    }

    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var3) {
            ;
        }

    }

    /** @deprecated */
    @Deprecated
    public static Date getFirstDayOfCurMonth() {
        return ApiDateUtil.getFirstDayOfCurMonth();
    }

    /** @deprecated */
    @Deprecated
    public static Date getFirstDayInMonth(Date date) {
        return ApiDateUtil.getFirstDayInMonth(date);
    }

    /** @deprecated */
    @Deprecated
    public static Date getFirstDayInMonth(int month) {
        return ApiDateUtil.getFirstDayInMonth(month);
    }

    /** @deprecated */
    @Deprecated
    public static boolean isCurrentMonth(Date date) {
        return ApiDateUtil.isCurrentMonth(date);
    }

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int seed) {
        return random.nextInt(seed);
    }

    /** @deprecated */
    @Deprecated
    public static String arrayToString(long[] ids) {
        return ArrayUtil.arrayToString(ids);
    }

    public static String getLangCode(int lang) {
        String l = (String)code2LangMap.get(Integer.valueOf(lang));
        return l != null?l:"zh-cn";
    }

    public static String toLangString(int lang) {
        return (String)code2LangMap.get(Integer.valueOf(lang));
    }

    public static Integer toLangCode(String lang) {
        return (Integer)lang2CodeMap.get(lang);
    }

    /** @deprecated */
    @Deprecated
    public static String toJSONStr(long[] ids) {
        return ArrayUtil.toJSONStr(ids);
    }

    /** @deprecated */
    @Deprecated
    public static final void reverse(long[] b) {
        ArrayUtil.reverse(b);
    }

    /** @deprecated */
    @Deprecated
    public static final void reverse(long[][] b) {
        ArrayUtil.reverse(b);
    }

    /** @deprecated */
    @Deprecated
    public static final long[] removeAll(long[] sourceArray, long[] removeArray) {
        return ArrayUtil.removeAll(sourceArray, removeArray);
    }

    public static String truncateString(Object value, int length) {
        String oldValue = value == null?"":value.toString();
        if(length > 0 && oldValue.length() > length) {
            oldValue = oldValue.substring(0, length);
        }

        return oldValue;
    }

    public static void main(String[] args) {
        int hash = getHash4split(10506L, 256);
        System.out.println(hash);
        Date date1 = parseDateTime("2011-12-23 11:57:48", (Date)null);
        Date date2 = parseDateTime("2011-12-22 11:57:48", (Date)null);
        System.out.println("date1 timeMillis:" + date1.getTime());
        System.out.println("date2 timeMillis:" + date2.getTime());
        System.out.println("date1-date2 timeMillis:" + (date1.getTime() - date2.getTime()));
    }

    static {
        genders.put(Byte.valueOf((byte)1), "m");
        genders.put(Byte.valueOf((byte)2), "f");
        hasVisible = new ThreadLocal() {
            protected Boolean initialValue() {
                return Boolean.valueOf(false);
            }
        };
        lang2CodeMap = new HashMap();
        code2LangMap = new HashMap();
        lang2CodeMap.put("zh-cn", Integer.valueOf(1));
        lang2CodeMap.put("zh-tw", Integer.valueOf(2));
        lang2CodeMap.put("zh-hk", Integer.valueOf(3));
        lang2CodeMap.put("en", Integer.valueOf(4));
        lang2CodeMap.put("en-us", Integer.valueOf(5));
        code2LangMap.put(Integer.valueOf(0), "zh-cn");
        code2LangMap.put(Integer.valueOf(1), "zh-cn");
        code2LangMap.put(Integer.valueOf(2), "zh-tw");
        code2LangMap.put(Integer.valueOf(3), "zh-hk");
        code2LangMap.put(Integer.valueOf(4), "en");
        code2LangMap.put(Integer.valueOf(5), "en-us");
    }
    
}
