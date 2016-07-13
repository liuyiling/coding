package com.liuyiling.java.tomcat.demo1.connector;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liuyl on 16/6/27.
 */
public class SocketInputStream extends InputStream {


    // -------------------------------------------------------------- Constants


    /**
     * CR.
     */
    private static final byte CR = (byte) '\r';


    /**
     * LF.
     */
    private static final byte LF = (byte) '\n';


    /**
     * SP.
     */
    private static final byte SP = (byte) ' ';


    /**
     * HT.
     */
    private static final byte HT = (byte) '\t';


    /**
     * COLON.
     */
    private static final byte COLON = (byte) ':';


    /**
     * Lower case offset.
     */
    private static final int LC_OFFSET = 'A' - 'a';


    /**
     * Internal buffer.
     */
    protected byte buf[];


    /**
     * Last valid byte.
     */
    protected int count;


    /**
     * Position in the buffer.
     */
    protected int pos;


    /**
     * Underlying input stream.
     */
    protected InputStream is;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet input stream associated with the specified socket
     * input.
     *
     * @param is socket input stream
     * @param bufferSize size of the internal buffer
     */
    public SocketInputStream(InputStream is, int bufferSize) {

        this.is = is;
        buf = new byte[bufferSize];
    }

    public SocketInputStream() {
        super();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n);
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}
