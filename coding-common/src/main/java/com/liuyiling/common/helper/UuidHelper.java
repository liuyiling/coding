package com.liuyiling.common.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuyl on 2016/12/29.
 */
public class UuidHelper {

    public static final long MIN_VALID_ID = 4632645677875266562L;
    public static final long MAX_VALID_ID = 7941609278668866563L;
    private static ThreadLocal<SimpleDateFormat> monthDateFmt = new ThreadLocal() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };
    public static final UuidSimulator uidSimulator = new UuidSimulator();

    public UuidHelper() {
    }

    public static boolean isValidId(long id) {
        return id > 4632645677875266562L && id < 7941609278668866563L;
    }

    public static long getIdByDate(Date date) {
        return uidSimulator.generateId(date.getTime());
    }

    public static Date getDateFromId(long id) {
        return new Date(getTimeFromId(id));
    }

    public static String getYearMonthFromId(long id) {
        return ((SimpleDateFormat)monthDateFmt.get()).format(getDateFromId(id));
    }

    public static long getTimeFromId(long id) {
        return getTimeNumberFromId(id);
    }

    public static long getTimeNumberFromId(long id) {
        return id >> 22;
    }

    public static long getSeqFromId(long id) {
        return id & 1023L;
    }

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        long id = getIdByDate(date);
        System.out.println(date);
        System.out.println(id);
        System.out.println(getDateFromId(id));
        System.out.println(getTimeNumberFromId(id));
        System.out.println(System.currentTimeMillis());
        System.out.println(getYearMonthFromId(id));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(getIdByDate(sdf.parse("2005-1-1")));
        System.out.println(getIdByDate(sdf.parse("2030-1-1")));
        System.out.println(sdf.parse("2005-1-1"));
        System.out.println(sdf.parse("2030-1-1"));
        System.out.println(getDateFromId(5940752390209733715L));
        System.out.println(getSeqFromId(id));
        System.out.println(getSeqFromId(getIdByDate(date)));
        System.out.println(isValidId(5940752390209733715L));
    }

}
