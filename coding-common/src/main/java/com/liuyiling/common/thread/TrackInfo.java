package com.liuyiling.common.thread;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by liuyl on 2016/12/20.
 */
public abstract class TrackInfo {

    public long start = System.currentTimeMillis();
    public long cost = -1L;

    public TrackInfo() {
    }

    public void complete() {
        this.cost = System.currentTimeMillis() - this.start;
    }

    public abstract JSONObject toJson();

}
