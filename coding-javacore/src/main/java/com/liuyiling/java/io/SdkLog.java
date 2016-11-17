package com.liuyiling.java.io;

import java.util.DoubleSummaryStatistics;

/**
 * Created by liuyl on 2016/10/25.
 */
public class SdkLog {

    public long liveId;
    public long uid;
    public long time;
    public long playTime;
    public String version;
    public String model;
    public Double audioFps;
    public Double videoFps;

    public static SdkLog parseFromStr(String lineTxt){
        String[] split = lineTxt.split("\t");
        String version = split[5];
        if(version.contains(".")){
            Long liveId = Long.valueOf(split[0]);
            Long uid = Long.valueOf(split[1]);
            Long time = Long.valueOf(split[3]);
            Long playTime = Long.valueOf(split[4]);
            String model = split[6];
            Double audioFps = Double.valueOf(split[7]);
            Double videoFps = Double.valueOf(split[8]);
            SdkLog sdkLog = new SdkLog(liveId, uid, time, playTime, version, model, audioFps, videoFps);
            return sdkLog;
        }

        return null;
    }

    public SdkLog(long liveId, long uid, long time, long playTime, String version, String model, Double audioFps, Double videoFps) {
        this.liveId = liveId;
        this.uid = uid;
        this.time = time;
        this.playTime = playTime;
        this.version = version;
        this.model = model;
        this.audioFps = audioFps;
        this.videoFps = videoFps;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getAudioFps() {
        return audioFps;
    }

    public void setAudioFps(Double audioFps) {
        this.audioFps = audioFps;
    }

    public Double getVideoFps() {
        return videoFps;
    }

    public void setVideoFps(Double videoFps) {
        this.videoFps = videoFps;
    }
}
