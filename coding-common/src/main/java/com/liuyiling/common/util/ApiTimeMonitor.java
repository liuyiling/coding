package com.liuyiling.common.util;

import com.alibaba.fastjson.JSONObject;
import com.liuyiling.common.log.LogCollectorFactory;
import com.liuyiling.common.thread.RequestTraceContext;
import com.liuyiling.common.thread.ResourceTrackInfo;
import com.liuyiling.common.thread.TrackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/20.
 */
public class ApiTimeMonitor {

    public static final ApiTimeMonitor apiCommonMonitor = new ApiTimeMonitor("apiCommonMonitor");
    public static final ConcurrentHashMap<String, String> apiMap = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, ApiTimeMonitor> STATIC_MONITORS;
    public static final long MILES_FIVE_MINUTES = 30000L;
    private String name = null;
    private AtomicLong start = new AtomicLong(0L);
    private AtomicLong visitCount = new AtomicLong(0L);
    private AtomicLong totalMiles = new AtomicLong(0L);
    private AtomicLong randDelay = new AtomicLong(0L);
    private ConcurrentHashMap<String, ResourceTrackInfo> tracksMap = new ConcurrentHashMap();
    private static AtomicBoolean isPause;

    public static void setPause(boolean pause) {
        isPause.compareAndSet(!pause, pause);
    }

    public static boolean getStatus() {
        return isPause.get();
    }

    public ApiTimeMonitor(String moduleName) {
        this.name = moduleName;
        this.randDelay.compareAndSet(0L, (long)(30000.0D * Math.random()));
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void time() {
        long last = this.getCurrentTime();
        long start = this.start.get();
        if(start > 0L) {
            if(last - start >= 30000L) {
                long vc = this.visitCount.get();
                if(!isPause.get() && vc > 0L) {
                    try {
                        JSONObject e = new JSONObject();
                        e.put("total", vc);
                        e.put("avg", this.totalMiles.get() / vc);
                        if(!this.tracksMap.isEmpty()) {
                            ArrayList trackJsons = new ArrayList();
                            Iterator it = this.tracksMap.values().iterator();

                            while(it.hasNext()) {
                                ResourceTrackInfo track = (ResourceTrackInfo)it.next();
                                if(track != null) {
                                    if(track.count != 0) {
                                        track.cost /= (long)track.count;
                                    }

                                    trackJsons.add(track.toJsonWithCount());
                                }
                            }

                            e.put("tracks", trackJsons);
                        }

                        LogCollectorFactory.getLogCollector().log(this.name, "api", e.toString());
                    } catch (Exception var11) {
                        UniversalLogger.error("[ApiTimeMonitor] Stastics error!", var11);
                    }
                }

                this.totalMiles.set(0L);
                this.visitCount.set(0L);
                this.tracksMap.clear();
                this.start.set(0L);
            }

        }
    }

    public static ApiTimeMonitor getInstance(String info) {
        if(!hasApi(info)) {
            return null;
        } else {
            String key = "key_" + info;
            ApiTimeMonitor monitor = (ApiTimeMonitor)STATIC_MONITORS.get(key);
            if(monitor != null) {
                return monitor;
            } else {
                UniversalLogger.info("[ApiTimeMonitor] key is:" + key);
                monitor = new ApiTimeMonitor(info);
                STATIC_MONITORS.put(key, monitor);
                return monitor;
            }
        }
    }

    public static boolean hasApi(String api) {
        if(apiMap != null && !apiMap.isEmpty() && api != null) {
            Iterator it = apiMap.keySet().iterator();

            do {
                if(!it.hasNext()) {
                    return false;
                }
            } while(api.indexOf((String)it.next()) < 0);

            return true;
        } else {
            return false;
        }
    }

    public static void log(RequestTraceContext context, long time) {
        try {
            ApiTimeMonitor e = getInstance(context.getApi());
            boolean isprintstack = time >= 500L;
            if(e == null && !isprintstack) {
                return;
            }

            if(e != null) {
                e.track(context, time);
            }

            JSONObject json = null;
            if(isprintstack) {
                json = new JSONObject();
                json.put("api", context.getApi());
                json.put("id", context.getId());
            }

            Queue tracks = context.getTracks();
            if(tracks != null) {
                ArrayList log = null;
                if(isprintstack) {
                    log = new ArrayList();
                }

                while(!tracks.isEmpty()) {
                    TrackInfo track = (TrackInfo)tracks.poll();
                    apiResourceStatistics(e, track);
                    apiResourceTracks(log, track);
                }

                if(isprintstack) {
                    json.put("tracks", log);
                }
            }

            if(isprintstack) {
                String log1 = json.toString();
                LogCollectorFactory.getLogCollector().log("api", "request_id", log1);
                UniversalLogger.warn(log1);
            }
        } catch (Exception var9) {
            UniversalLogger.warn("ApiTimeMonitor error");
        }

    }

    private static void apiResourceTracks(List<JSONObject> jsons, TrackInfo track) {
        if(jsons != null && track != null && (track.cost > 10L || track.cost == -1L)) {
            jsons.add(track.toJson());
        }

    }

    private static void apiResourceStatistics(ApiTimeMonitor monitor, TrackInfo track) {
        if(track != null && monitor != null && track instanceof ResourceTrackInfo) {
            ResourceTrackInfo info = (ResourceTrackInfo)track;
            String key = monitor.getTrackKey(info);
            ResourceTrackInfo previous = (ResourceTrackInfo)monitor.tracksMap.putIfAbsent(key, info);
            if(previous != null) {
                previous.cost += info.cost;
                ++previous.count;
            } else {
                ++info.count;
                monitor.tracksMap.put(key, info);
            }
        }

    }

    public void track(RequestTraceContext context, long time) {
        this.visitCount.addAndGet(1L);
        this.totalMiles.addAndGet(time);
        if(this.start.get() <= 0L) {
            this.start.set(this.getCurrentTime());
        }

        this.time();
    }

    public String getTrackKey(ResourceTrackInfo track) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(track.type);
        buffer.append("_");
        buffer.append(track.port);
        return buffer.toString();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String cacheName) {
        this.name = cacheName;
    }

    public long getEndMiles() {
        return this.totalMiles.get();
    }

    public void setEndMiles(long endMiles) {
        this.totalMiles = new AtomicLong(endMiles);
    }

    public long getMinute1TotalCount() {
        return this.visitCount.get();
    }

    public void setMinute1TotalCount(long minute1TotalCount) {
        this.visitCount = new AtomicLong(minute1TotalCount);
    }

    static {
        apiMap.put("statuses/friends_timeline", "1");
        apiMap.put("statuses/intelligence_timeline", "2");
        STATIC_MONITORS = new ConcurrentHashMap(1000);
        isPause = new AtomicBoolean(false);
    }

}
