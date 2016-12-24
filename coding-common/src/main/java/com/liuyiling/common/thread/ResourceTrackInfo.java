package com.liuyiling.common.thread;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by liuyl on 2016/12/20.
 */
public class ResourceTrackInfo extends TrackInfo{

    public int type;
    public int port;
    public int count = 0;
    public boolean isWrite;

    public ResourceTrackInfo(int type, int port, boolean isWrite) {
        this.type = type;
        this.port = port;
        this.isWrite = isWrite;
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + this.port;
        result1 = 31 * result1 + this.type;
        return result1;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            ResourceTrackInfo other = (ResourceTrackInfo)obj;
            return this.port != other.port?false:this.type == other.type;
        }
    }

    public JSONObject toJson() {
        JSONObject builder = new JSONObject();
        if(this.type == 1000000) {
            builder.put("type", "MC");
        } else if(this.type == 3000000) {
            builder.put("type", "DB");
        } else if(this.type == 2000000) {
            builder.put("type", "REDIS");
        }

        builder.put("port", this.port);
        builder.put("write", this.isWrite);
        builder.put("start", this.start);
        builder.put("cost", this.cost);
        return builder;
    }

    public JSONObject toJsonWithCount() {
        JSONObject builder = new JSONObject();
        if(this.type == 1000000) {
            builder.put("type", "MC");
        } else if(this.type == 3000000) {
            builder.put("type", "DB");
        } else if(this.type == 2000000) {
            builder.put("type", "REDIS");
        }

        builder.put("port", this.port);
        builder.put("cost", this.cost);
        builder.put("total", this.count);
        return builder;
    }

}
