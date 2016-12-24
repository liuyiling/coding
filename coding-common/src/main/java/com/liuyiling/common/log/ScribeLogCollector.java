package com.liuyiling.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * prometheus所需要的日志格式
 * Created by liuyl on 2016/12/20.
 */
public class ScribeLogCollector implements LogCollector{

    private static final Logger LOG = LoggerFactory.getLogger(ScribeLogCollector.class);
    private static final Logger scribe = LoggerFactory.getLogger("openapi_prism");

    public ScribeLogCollector() {
    }

    public void log(String logId, String module, String schema, DType type, Object data) {
        if(type == DType.RAWSTR) {
            this.logRawStr(logId, module, schema, (String)data);
        } else if(type == DType.JSON) {
            this.logJson(logId, module, schema, (String)data);
        } else {
            LOG.warn(String.format("Unsupport type(%s, %s, %s, %d)", new Object[]{logId, module, schema, Integer.valueOf(type.value())}));
        }

    }

    public void log(String module, String schema, String json) {
        this.log("0", module, schema, DType.JSON, json);
    }

    public void logRawStr(String logId, String module, String schema, String data) {
        this.log0(logId, module, schema, DType.RAWSTR, data);
    }

    public void logJson(String logId, String module, String schema, String json) {
        this.log0(logId, module, schema, DType.JSON, json);
    }

    private void log0(String logId, String module, String schema, DType type, String data) {
        if(scribe.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder(128);
            sb.append(logId);
            sb.append(" ").append(module);
            sb.append(" ").append(schema);
            sb.append(" ").append(type.value());
            sb.append(" ").append(data);
            scribe.info(sb.toString());
        }
    }

}
