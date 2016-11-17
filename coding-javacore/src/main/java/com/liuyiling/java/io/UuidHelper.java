/**
 * 
 */
package com.liuyiling.java.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基于最新64位发号器改造的java版本实现
 * 
 * @author XiaoJunHong
 *
 */
public class UuidHelper {

    public static final long MIN_VALID_ID = 4632645677875266562l; //2005-1-1
    public static final long MAX_VALID_ID = 7941609278668866563l; //2030-1-1

    private static ThreadLocal<SimpleDateFormat> monthDateFmt = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };

    public static boolean isValidId(long id) {
        return (id > MIN_VALID_ID) && (id < MAX_VALID_ID);
    }

    public static final UuidSimulator uidSimulator = new UuidSimulator();
    

    public static long getIdByDate(Date date) {
        return uidSimulator.generateId(date.getTime());
    }
    
    public static Date getDateFromId(long id){
    	return new Date(getTimeFromId(id));
    }

    /**
     * 给定id,返回这个id所代表的年度月份
     * @param id
     * @return yyyyMM
     */
    public static String getYearMonthFromId(long id) {
        return monthDateFmt.get().format(getDateFromId(id));
    }
    
    public static long getTimeFromId(long id) {
        return getTimeNumberFromId(id); 
    }
    
    public static long getTimeNumberFromId(long id) {
        return id >> UuidConst.BIT_IDC_LENGTH;
    }

    public static long getSeqFromId(long id) {
        return id & (0x3FF);
    }
    
    public static void main(String[] args) throws ParseException{
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
    	
    	System.out.println(getDateFromId(5940752390209733715l));
    	System.out.println(getSeqFromId(id));
    	System.out.println(getSeqFromId(getIdByDate(date)));

    	System.out.println(isValidId(5940752390209733715l));
    }
}
