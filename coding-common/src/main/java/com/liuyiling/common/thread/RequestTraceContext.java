package com.liuyiling.common.thread;

import com.alibaba.fastjson.JSONObject;
import com.liuyiling.common.util.ApiTimeMonitor;
import com.liuyiling.common.util.IPUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/20.
 */
public class RequestTraceContext {

    private static final ThreadLocal<RequestTraceContext> requestContext = new ThreadLocal();
    private static final AtomicLong idGenerator = new AtomicLong();
    private static final ResourceTrackInfo EMPTY_RESOURCE_TRACK = new ResourceTrackInfo(-1, -1, false);
    private static long refreshIdPrefix = 0L;
    private long id;
    private String requestId;
    private String api;
    private long start;
    private long uid;
    private String appKey;
    private String originalSource;
    private int appId;
    private String spr;
    private Queue<TrackInfo> tracks;
    private Map<String, Object> contextState;

    public RequestTraceContext(String api) {
        this(api, 0L, (String)null, 0, (String)null);
    }

    public RequestTraceContext(String api, String requestId) {
        this(api, 0L, (String)null, 0, (String)null);
        this.requestId = requestId;
    }

    public RequestTraceContext(String api, long uid, String appKey, int appId, String originalSource) {
        this.tracks = new ConcurrentLinkedQueue();
        this.contextState = new HashMap();
        this.api = api;
        this.id = refreshIdPrefix | idGenerator.incrementAndGet();
        this.start = System.currentTimeMillis();
        this.uid = uid;
        this.appKey = appKey;
        this.appId = appId;
        this.originalSource = originalSource;
    }

    public static void init(String api) {
        requestContext.set(new RequestTraceContext(api));
    }

    public static void init(String api, String requestId) {
        requestContext.set(new RequestTraceContext(api, requestId));
    }

    public static void init(String api, long uid, String appKey, int appId, String originalSource) {
        requestContext.set(new RequestTraceContext(api, uid, appKey, appId, originalSource));
    }

    public static void spawn(RequestTraceContext parentContext) {
        requestContext.set(parentContext);
    }

    public static void clear() {
        requestContext.remove();
    }

    public static RequestTraceContext get() {
        return (RequestTraceContext)requestContext.get();
    }

    public static void finish() {
        RequestTraceContext context = get();
        if(context != null) {
            long timemillis = System.currentTimeMillis() - context.start;
            ApiTimeMonitor.log(context, timemillis);
            clear();
        }
    }

    public static boolean isPC() {
        return checkOriginalSource("3818214747");
    }

    public static boolean isMAPI() {
        return checkOriginalSource("3439264077");
    }

    public static boolean checkOriginalSource(String appkey) {
        if(StringUtils.isBlank(appkey)) {
            return false;
        } else {
            RequestTraceContext context = get();
            return context != null?appkey.equals(context.getOriginalSource()):false;
        }
    }

    public static String getRequestId() {
        RequestTraceContext context = get();
        return context != null?context.requestId:null;
    }

    public static long getRefreshId() {
        RequestTraceContext context = get();
        return context != null?context.id:0L;
    }

    public static boolean isFromApi(String api) {
        if(StringUtils.isBlank(api)) {
            return false;
        } else {
            RequestTraceContext context = get();
            return context != null?api.equals(context.getApi()):false;
        }
    }

    public static ResourceTrackInfo trackResource(int type, int port, boolean isWrite) {
        ResourceTrackInfo trackInfo = null;
        RequestTraceContext context = get();
        if(context != null) {
            trackInfo = new ResourceTrackInfo(type, port, isWrite);
            context.tracks.add(trackInfo);
        } else {
            trackInfo = EMPTY_RESOURCE_TRACK;
        }

        return trackInfo;
    }

    public String printTrack() {
        JSONObject json = new JSONObject();
        json.put("api", this.api);
        json.put("id", this.id);
        if(this.tracks != null) {
            ArrayList trackJsons = new ArrayList();

            while(true) {
                TrackInfo track;
                do {
                    do {
                        if(this.tracks.isEmpty()) {
                            json.put("tracks", trackJsons);
                            return json.toString();
                        }

                        track = (TrackInfo)this.tracks.poll();
                    } while(track == null);
                } while(track.cost <= 10L && track.cost != -1L);

                trackJsons.add(track.toJson());
            }
        } else {
            return json.toString();
        }
    }

    public long getId() {
        return this.id;
    }

    public String getApi() {
        return this.api;
    }

    public Queue<TrackInfo> getTracks() {
        return this.tracks;
    }

    public long getUid() {
        return this.uid;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public int getAppId() {
        return this.appId;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getOriginalSource() {
        return this.originalSource;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    public String getSpr() {
        return this.spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }

    public Map<String, Object> getContextState() {
        return this.contextState;
    }

    public void setContextState(Map<String, Object> contextState) {
        this.contextState = contextState;
    }

    static {
        int intIp = IPUtils.ipToInt(IPUtils.getLocalIp());
        intIp &= 16777215;
        intIp <<= 6;
        int minute = Calendar.getInstance().get(12);
        refreshIdPrefix = (long)(intIp | minute) << 33;
    }
}
