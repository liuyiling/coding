package com.liuyiling.common.helper;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/29.
 */
public class UuidSimulator {
    private int ID_INTEVAL;
    private int seqSize;
    private Map<Long, AtomicLong> seqMap;

    public UuidSimulator() {
        this(50);
    }

    public UuidSimulator(int seqSize) {
        this.ID_INTEVAL = 1;
        this.seqSize = seqSize;
        this.seqMap = Collections.synchronizedMap(new LinkedHashMap(seqSize));
    }

    public long generateId(long time) {
        AtomicLong atomicLong = (AtomicLong)this.seqMap.get(Long.valueOf(time));
        if(atomicLong == null) {
            synchronized(this) {
                atomicLong = (AtomicLong)this.seqMap.get(Long.valueOf(time));
                if(atomicLong == null) {
                    if(this.seqMap.size() >= this.seqSize) {
                        Iterator it = this.seqMap.keySet().iterator();
                        if(it.hasNext()) {
                            Long eldestKey = (Long)it.next();
                            this.seqMap.remove(eldestKey);
                        }
                    }

                    atomicLong = new AtomicLong();
                    this.seqMap.put(Long.valueOf(time), atomicLong);
                }
            }
        }

        return this.generateId(time, atomicLong.getAndAdd((long)this.ID_INTEVAL));
    }

    private long generateId(long time, long seq) {
        long uuid = time << 22;
        uuid += 65536L;
        uuid += 1024L;
        uuid += seq % 1024L;
        return uuid;
    }

    public static void main(String[] args) {
        final UuidSimulator s = new UuidSimulator();
        final long time = System.currentTimeMillis();
        Runnable r = new Runnable() {
            public void run() {
                int i = 0;

                while(true) {
                    long cur_time = System.currentTimeMillis();
                    if(cur_time - time >= 1000L) {
                        System.out.println(i);
                        System.exit(0);
                    }

                    s.generateId(cur_time);
                    ++i;
                }
            }
        };
        (new Thread(r)).start();
    }

}
