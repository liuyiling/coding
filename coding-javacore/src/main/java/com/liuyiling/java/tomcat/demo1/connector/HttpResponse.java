package com.liuyiling.java.tomcat.demo1.connector;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by liuyl on 16/6/27.
 */
public class HttpResponse {

    private static final int BUFFER_SIZE = 1024;
    HttpRequest request;
    OutputStream output;
    PrintWriter writer;
    protected byte[] buffer = new byte[BUFFER_SIZE];
    protected int bufferCount = 0;
    protected HashMap headers = new HashMap();

    public void setHeader(String name, String value){
    }

    public void finisheResponse(){
        if(writer != null){
            writer.flush();
            writer.close();
        }
    }

    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getBufferCount() {
        return bufferCount;
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public void setHeaders(HashMap headers) {
        this.headers = headers;
    }

    public void finishResponse() {
    }
}
