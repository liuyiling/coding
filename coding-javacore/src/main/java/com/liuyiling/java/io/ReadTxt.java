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

        initUidUserName();
        imBreak();

        int yyMMdd = 160915;

        for (int i = yyMMdd; i <= 161024; i++) {

            String fileName = "/Users/meitu/workspace/gitlab.meitu.com/statistic_live_report/" + i + ".log";
            File file = new File(fileName);
            String encoding = "UTF-8";
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    SdkLog sdkLog = SdkLog.parseFromStr(lineTxt);

                    if(sdkLog == null){
                        continue;
                    }

                    long uid = sdkLog.getUid();
                    Long liveId = sdkLog.getLiveId();
                    List<Long> liveIdList = uidLiveIdListMap.get(uid);
                    if (liveIdList == null) {
                        liveIdList = new ArrayList<>();
                        uidLiveIdListMap.put(uid, liveIdList);
                    }
                    if(!liveIdList.contains(liveId)){
                        liveIdList.add(liveId);
                    }

                    List<SdkLog> sdkLogList = liveIdSdkLogListMap.get(sdkLog.getLiveId());
                    if (sdkLogList == null) {
                        sdkLogList = new ArrayList<>();
                        liveIdSdkLogListMap.put(liveId, sdkLogList);
                    }
                    sdkLogList.add(sdkLog);
                }
            }

            if (i == 160930) {
                i = 161001;
                continue;
            }
        }
        for(Map.Entry<Long, String> entry : uidUserNameMap.entrySet()){
            long uid = entry.getKey();
            String userName = entry.getValue();
            List<Long> liveIdList = uidLiveIdListMap.get(uid);
            if(liveIdList == null){
                continue;
            }
            //uid ,              用户名,                                 日期 ,         版本,   开始时间,  时长(分钟),   卡顿率, 最后2分钟卡顿率
            //68891754 ,   Mc军刺一个男人❤️,                             2016.9.15,  5.3.0 , 8:00,          65,                 4.3%,   7.2%
            for(long liveId : liveIdList){
                List<SdkLog> sdkLogs = liveIdSdkLogListMap.get(liveId);
                long maxTime = 0;
                long duration = 0;
                long minTime = 0;
                String version = "";
                int logTotal = 0;
                int lowFpsTotal = 0;
                int lastTwoMinuteLogTotal = 0;
                int lastTwoMinuteLowFpsTotal = 0;
                for(SdkLog sdkLog : sdkLogs){
                    version = sdkLog.getVersion();
                    long playTime = sdkLog.getPlayTime();
                    if(playTime > maxTime) maxTime = playTime;
                    if(playTime < minTime) minTime = playTime;
                }
                duration = maxTime;

                int hasAudioNoVideoTotal = 0;
                int videoAudioTotal = 0;
                for(SdkLog sdkLog : sdkLogs){
                    logTotal++;
                    if(sdkLog.getVideoFps() < 20) lowFpsTotal++;
                    //计算最后2分钟卡顿率
                    if(sdkLog.getPlayTime() > (duration - 120)){
                        if(sdkLog.getVideoFps() < 16 && sdkLog.getAudioFps() > 40){
                            hasAudioNoVideoTotal++;
                        }
                        videoAudioTotal++;
                    }
                }

                Double bufferPercent = lowFpsTotal * 1.0 / logTotal * 100;
                Double lastTwoMinuteBufferPercent = lastTwoMinuteLowFpsTotal * 1.0 / lastTwoMinuteLogTotal * 100;
                Date dateFromId = UuidHelper.getDateFromId(liveId);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(dateFromId);
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                String time = simpleDateFormat.format(dateFromId);

                Integer maxBreak = maxBreakMap.get(liveId);
                Integer breakCount = breakCountMap.get(liveId);
                Integer totalBreak = totalBreakMap.get(liveId);

                if(videoAudioTotal !=0 && hasAudioNoVideoTotal * 1.0 / videoAudioTotal > 0.5){
                    String resultStr = uid + "," + userName + "," + liveId + "," + date + "," + time + "\n";
                    resultStrList.add(resultStr);
                }

            }
        }

        long sum = 0;
        for(Map.Entry<Long, List<Long>> entry : uidLiveIdListMap.entrySet()){
            sum += entry.getValue().size();
        }

        try {

            File file = new File("/Users/meitu/statistic.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(String temp : resultStrList){
                bufferedWriter.write(temp);
            }
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void initUidUserName() throws IOException {
        String fileName = "/Users/meitu/workspace/gitlab.meitu.com/statistic_live_report/uid_username";
        File file = new File(fileName);
        String encoding = "UTF-8";
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] split = lineTxt.split("\t");
                uidUserNameMap.put(Long.valueOf(split[0]), split[1]);
            }
        }
    }

    public static void imBreak() throws IOException {
        String fileName = "/Users/meitu/workspace/gitlab.meitu.com/statistic_live_report/im_break";
        File file = new File(fileName);
        String encoding = "UTF-8";
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] split = lineTxt.split("\t");
                long liveId = Long.valueOf(split[0]);
                Integer maxBreak = Integer.valueOf(split[1]);
                maxBreakMap.put(liveId, maxBreak);
                Integer breakCount = Integer.valueOf(split[2]);
                breakCountMap.put(liveId, breakCount);
                Integer totalBreak = Integer.valueOf(split[3]);
                totalBreakMap.put(liveId, totalBreak);
            }
        }
    }
}
