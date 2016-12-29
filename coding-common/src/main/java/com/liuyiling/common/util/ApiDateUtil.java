package com.liuyiling.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liuyl on 2016/12/29.
 */
public class ApiDateUtil {

    private static ThreadLocal<DateFormat> formatter = new ThreadLocal() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        }
    };
    private static ThreadLocal<DateFormat> yearMonthSdf = new ThreadLocal() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yy_MM", Locale.ENGLISH);
        }
    };
    private static ThreadLocal<DateFormat> yearMonthDaySdf = new ThreadLocal() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static ThreadLocal<DateFormat> dateTimeSdf = new ThreadLocal() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final int DAY_IN_MILLIONSECONDS = 86400000;

    public ApiDateUtil() {
    }

    public static String formatDate(Date date, String defaultValue) {
        if(date == null) {
            return defaultValue;
        } else {
            try {
                return ((DateFormat)formatter.get()).format(date);
            } catch (RuntimeException var3) {
                UniversalLogger.error((new StringBuilder(64)).append("Error: in ApiUtil.formatDate, date=").append(date).append(", default_value=").append(defaultValue).toString(), var3);
                return null;
            }
        }
    }

    public static Date parseDate(String dateStr, Date defaultValue) {
        if(dateStr == null) {
            return defaultValue;
        } else {
            try {
                return ((DateFormat)formatter.get()).parse(dateStr);
            } catch (ParseException var3) {
                return defaultValue;
            }
        }
    }

    public static String getYearMonth(Date date) {
        return ((DateFormat)yearMonthSdf.get()).format(date);
    }

    public static String formateYearMonthDay(Date date) {
        return ((DateFormat)yearMonthDaySdf.get()).format(date);
    }

    public static Date parseYearMonthDay(String timeStr, Date defaultValue) {
        if(timeStr == null) {
            return defaultValue;
        } else {
            try {
                return ((DateFormat)yearMonthDaySdf.get()).parse(timeStr);
            } catch (ParseException var3) {
                return defaultValue;
            }
        }
    }

    public static String formateDateTime(Date date) {
        return ((DateFormat)dateTimeSdf.get()).format(date);
    }

    public static Date parseDateTime(String timeStr, Date defaultValue) {
        if(timeStr == null) {
            return defaultValue;
        } else {
            try {
                return ((DateFormat)dateTimeSdf.get()).parse(timeStr);
            } catch (ParseException var3) {
                return defaultValue;
            }
        }
    }

    public static final int getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(11);
    }

    public static final int getLastHour() {
        int hour = getCurrentHour();
        return hour == 0?23:hour - 1;
    }

    public static final int getNextHour() {
        int hour = getCurrentHour();
        return hour == 23?0:hour + 1;
    }

    public static Date getFirstDayOfCurMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static Date getFirstDayInMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if(date != null) {
            calendar.setTime(date);
        }

        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static long get1stDayOfMonthTimeInMillis(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    public static Date getFirstDayInMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.add(2, month);
        return getFirstDayInMonth(c.getTime());
    }

    public static boolean isCurrentMonth(Date date) {
        if(date == null) {
            return false;
        } else {
            Calendar dest = Calendar.getInstance();
            dest.setTime(date);
            Calendar now = Calendar.getInstance();
            return now.get(1) == dest.get(1) && now.get(2) == dest.get(2);
        }
    }

    public static int daysBetween(long from, long to) {
        return Math.abs(Math.round((float)((to - from) / 86400000L)));
    }

}
