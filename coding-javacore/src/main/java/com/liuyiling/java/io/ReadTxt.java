package com.liuyiling.java.io;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liuyl on 15/12/2.
 * 读txt
 */
public class ReadTxt {

    //key->uid,value->userName
    public static Map<Long, String> uidUserNameMap = new LinkedHashMap<>();
    //key->uid,value->liveIdList
    public static Map<Long, List<Long>> uidLiveIdListMap = new LinkedHashMap<>();
    public static Map<Long, List<SdkLog>> liveIdSdkLogListMap = new HashMap<>();
    public static List<String> resultStrList = new ArrayList<>();

    public static Map<Long, Integer> maxBreakMap = new HashMap<>();
    public static Map<Long, Integer> breakCountMap = new HashMap<>();
    public static Map<Long, Integer> totalBreakMap = new HashMap<>();

    public static void main(String[] agrs) throws IOException {


    }
}
