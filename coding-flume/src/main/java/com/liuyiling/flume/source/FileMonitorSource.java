package com.liuyiling.flume.source;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.instrumentation.SourceCounter;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by liuyl on 16/4/13.
 * 实现类似于exec tail -f 的功能
 */
public class FileMonitorSource extends AbstractSource implements Configurable, PollableSource {

    private static final Logger log = LoggerFactory.getLogger(FileMonitorSource.class);
    private ChannelProcessor channelProcessor;
    private RandomAccessFile monitorFile = null;
    private File coreFile = null;
    private long lastMod = 0L;
    private String monitorFilePath = null;
    private String positionFilePath = null;
    private FileChannel monitorFileChannel = null;
    private ByteBuffer buffer = ByteBuffer.allocate(1 << 20);
    private long positionValue = 0L;
    private ScheduledExecutorService executor;
    private FileMonitorThread runner;
    private PositionLog positionLog = null;
    private Charset charset = null;
    private CharsetDecoder decoder = null;
    private CharBuffer charBuffer = null;
    private long counter = 0L;
    private Map<String, String> headers = new HashMap<>();

    private Object exeLock = new Object();
    private long lastFileSize = 0L;
    private long nowFileSize = 0L;

    private SourceCounter sourceCounter = null;


    /**
     * 该线程用于检查文件是否有被修改的地方
     */
    class FileMonitorThread implements Runnable {

        @Override
        public void run() {
            synchronized (exeLock) {
                log.debug("FilemonitorThread is running ...");
                long nowModified = coreFile.lastModified();
                if ( lastMod != nowModified ) {
                    log.debug("File be modified---------------------------");
                    lastMod = nowModified;
                    nowFileSize = coreFile.length();
                    int readDataByteLen = 0;

                    try {
                        log.debug("The Last coreFileSize {}, now coreFileSize{}", lastFileSize, nowFileSize);

                        //日志文件被rolling处理过,例如info.log被roll成info.log + info.log-20160412
                        if (nowFileSize <= lastFileSize) {
                            log.debug("The file size is changed to be lower,it indicated that the file is rolled");
                            positionValue = 0L;
                        }

                        lastFileSize = nowFileSize;
                        monitorFile = new RandomAccessFile(coreFile, "r");
                        monitorFileChannel = monitorFile.getChannel();
                        monitorFileChannel.position(positionValue);

                        //读文件到缓冲区
                        int bytesRead = monitorFileChannel.read(buffer);
                        while (bytesRead != -1) {
                            log.debug("How many bytes read in this loop? -- > {}", bytesRead);
                            String contents = buffer2String(buffer);
                            int lastLineBreak = contents.lastIndexOf("\n") + 1;
                            String readData = contents.substring(0, lastLineBreak);
                            byte[] readDataBytes = readData.getBytes();
                            readDataByteLen = readDataBytes.length;
                            positionValue += readDataByteLen;

                            monitorFileChannel.position(positionValue);
                            log.debug("Read bytes {}, Read read bytes {}", bytesRead, readDataByteLen);

                            headers.put(Constants.KEY_DATA_SIZE, String.valueOf(readDataByteLen));
                            headers.put(Constants.KEY_DATA_LINE, String.valueOf(readData.split("\n")));

                            sourceCounter.incrementEventReceivedCount();
                            channelProcessor.processEvent(EventBuilder.withBody(readDataBytes, headers));
                            sourceCounter.addToEventAcceptedCount(1);
                            log.debug("Change the next read position{}", positionValue);
                            buffer.clear();
                            bytesRead = monitorFileChannel.read(buffer);
                        }

                    } catch (Exception e) {
                        log.error("Read data into channel error");
                        log.debug("Save the last positionValue {} into Disk File", positionValue - readDataByteLen);
                        positionLog.setPosition(positionValue - readDataByteLen);
                    }
                    counter++;
                    if (counter % Constants.POSITION_SAVE_COUNTER == 0) {
                        log.debug(Constants.POSITION_SAVE_COUNTER + " times file modified checked,save the position Value {} into Disk file", positionValue);
                        positionLog.setPosition(positionValue);
                    }

                }
            }
        }

        private String buffer2String(ByteBuffer buffer) {
            return "";
        }
    }

    @Override
    public void configure(Context context) {

    }

    @Override
    public Status process() throws EventDeliveryException {
        return null;
    }
}
