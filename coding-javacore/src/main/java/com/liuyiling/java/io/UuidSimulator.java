/**
 *
 */
package com.liuyiling.java.io;
import com.liuyiling.java.io.UuidConst;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 改造完之后 每个时间戳对应的seq都是独立的
 * 为了线程安全, 使用了java.util.Collections#synchronizedMap(java.util.Map)
 * 每秒产生7379678, 还是够用的
 */
public class UuidSimulator {

    private int ID_INTEVAL = 1;

//    private AtomicLong seq = new AtomicLong();

    private int seqSize;

    private Map<Long, AtomicLong> seqMap;

    public UuidSimulator() {
        this(50);
    }

    public UuidSimulator(int seqSize) {
        this.seqSize = seqSize;
        this.seqMap = Collections.synchronizedMap(new LinkedHashMap<Long, AtomicLong>(seqSize));
    }

    /**
     * generate default uuid
     * @param time
     * @return
     */
    public long generateId(long time){
        AtomicLong atomicLong = seqMap.get(time);
        if (atomicLong == null) {
            synchronized (this) {
                atomicLong = seqMap.get(time);
                if (atomicLong == null) {
                    if (seqMap.size() >= seqSize) {
                        Iterator<Long> it = seqMap.keySet().iterator();
                        if (it.hasNext()) {
                            Long eldestKey = it.next();
                            seqMap.remove(eldestKey);
                        }
                    }
                    atomicLong = new AtomicLong();
                    seqMap.put(time, atomicLong);
                }
            }
        }

        return generateId(time, atomicLong.getAndAdd(ID_INTEVAL));
    }

    private long generateId(long time, long seq){
        long uuid = time;
        uuid <<= UuidConst.BIT_IDC_LENGTH;
        uuid += 1 << UuidConst.BIT_BIZ_LENGTH;
        uuid += 1 << UuidConst.BIT_SEQUENCE_LENGTH;
        uuid += seq % UuidConst.SEQ_LIMIT;

        return uuid;
    }

    public static void main(String[] args) {
        final UuidSimulator s = new UuidSimulator();
        final long time = System.currentTimeMillis();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; ; i++) {
                    long cur_time = System.currentTimeMillis();
                    if (cur_time - time >= 1000) {
                        System.out.println(i);
                        System.exit(0);
                    }
                    s.generateId(cur_time);
                }
            }
        };
        new Thread(r).start();
//        new Thread(r).start();

//        long time1 = System.currentTimeMillis();
//        System.out.println(s.generateId(time1) & 0x3FF);
//        System.out.println(s.generateId(time1) & 0x3FF);
//
//        Map<Integer, Long> map = new HashMap<Integer, Long>();
//        for (int i = 0; i < 98; i++) {
//            long time;
//            int key = i % 49;
//            if (map.containsKey(key)) {
//                time = map.get(key);
//            } else {
//                time = System.currentTimeMillis();
//                map.put(key, time);
//            }
//            long id = s.generateId(time);
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(id & 0x3FF);
//        }
//
//        long time2 = System.currentTimeMillis();
//        System.out.println(s.generateId(time2) & 0x3FF);
//        System.out.println(s.generateId(time2) & 0x3FF);
//
//        System.out.println(s.generateId(time1) & 0x3FF);
    }
}
