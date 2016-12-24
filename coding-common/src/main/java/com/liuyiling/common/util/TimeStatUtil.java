package com.liuyiling.common.util;

import com.alibaba.fastjson.JSONObject;
import com.liuyiling.common.log.LogCollectorFactory;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/21.
 */
public class TimeStatUtil {
    public static final int MC_TYPE = 1000000;
    public static final int REDIS_TYPE = 2000000;
    public static final int DB_TYPE = 3000000;
    public static final int HBASE_TYPE = 4000000;
    public static final int[] TIMEARR = new int[]{1, 5, 10, 20, 30, 50, 100, 200, 300, 500};
    public static final int MULTI_TYPE = 100000;
    public static final int TOTAL_COUNT = 0;
    public static final int ERROR_COUNT = -1;
    private static int max_try_time = 2;
    public static ConcurrentHashMap<Integer, TimeStat> map = new ConcurrentHashMap();
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, AtomicLong>> retryMap = new ConcurrentHashMap();
    private static Thread printThread = new Thread("TimeStatUtil") {
        public void run() {
            while(!TimeStatUtil.isStopped) {
                TimeStatUtil.printMap();

                try {
                    Thread.sleep(300000L);
                } catch (InterruptedException var2) {
                    ;
                }
            }

        }
    };
    private static boolean isStopped;

    public TimeStatUtil() {
    }

    public static void setMaxTryTime(int retry) {
        max_try_time = retry;
    }

    public static void addElapseTimeStat(int name, int type) {
        try {
            name = name > 100000?name - 100000:name;
            if(retryMap.get(Integer.valueOf(name)) == null) {
                ConcurrentHashMap newMap = new ConcurrentHashMap();
                newMap.put(Integer.valueOf(0), new AtomicLong(0L));
                newMap.put(Integer.valueOf(-1), new AtomicLong(0L));

                for(int i = 1; i < max_try_time + 1; ++i) {
                    newMap.put(Integer.valueOf(i), new AtomicLong(0L));
                }

                retryMap.put(Integer.valueOf(name), newMap);
            }

            ((AtomicLong)((ConcurrentHashMap)retryMap.get(Integer.valueOf(name))).get(Integer.valueOf(type))).incrementAndGet();
        } catch (Exception var4) {
            ;
        }

    }

    public static void printRetryMap() {
        Iterator var0 = retryMap.keySet().iterator();

        while(var0.hasNext()) {
            int key = ((Integer)var0.next()).intValue();
            ConcurrentHashMap test = (ConcurrentHashMap)retryMap.get(Integer.valueOf(key));
            ConcurrentHashMap newMap = new ConcurrentHashMap();
            newMap.put(Integer.valueOf(0), new AtomicLong(0L));
            newMap.put(Integer.valueOf(-1), new AtomicLong(0L));

            for(int sb = 1; sb < max_try_time + 1; ++sb) {
                newMap.put(Integer.valueOf(sb), new AtomicLong(0L));
            }

            retryMap.put(Integer.valueOf(key), newMap);
            StringBuilder var6 = (new StringBuilder("resourceInterval ")).append(key - 2000000);
            var6.append(" ").append(test.get(Integer.valueOf(0)));

            for(int i = 1; i < max_try_time + 1; ++i) {
                var6.append(" ").append(test.get(Integer.valueOf(i)));
            }

            var6.append(" ").append(test.get(Integer.valueOf(-1)));
            UniversalLogger.info(var6.toString());
        }

    }

    public static void printMap() {
        Enumeration en = map.keys();

        while(true) {
            Integer key;
            TimeStatUtil.TimeStat timeStat;
            do {
                if(!en.hasMoreElements()) {
                    return;
                }

                key = (Integer)en.nextElement();
                timeStat = (TimeStatUtil.TimeStat)map.get(key);
            } while(timeStat.wTotal.get() <= 0L && timeStat.rTotal.get() <= 0L);

            map.put(key, new TimeStatUtil.TimeStat());

            try {
                printStat(key.intValue(), timeStat);
            } catch (Exception var4) {
                ;
            }
        }
    }

    public static void register(int name) {
        if(map.get(Integer.valueOf(name)) == null) {
            map.putIfAbsent(Integer.valueOf(name), new TimeStatUtil.TimeStat());
            map.putIfAbsent(Integer.valueOf(name + 100000), new TimeStatUtil.TimeStat());
        }

    }

    private static TimeStatUtil.TimeStat getTimeStat(int name) {
        TimeStatUtil.TimeStat timeStat = (TimeStatUtil.TimeStat)map.get(Integer.valueOf(name));
        if(timeStat == null) {
            register(name);
            return (TimeStatUtil.TimeStat)map.get(Integer.valueOf(name));
        } else {
            return timeStat;
        }
    }

    public static void addElapseTimeStat(int name, boolean isWriter, long startTime, long cost) {
        try {
            TimeStatUtil.TimeStat timeStat = getTimeStat(name);
            if(cost == -1L) {
                cost = System.currentTimeMillis() - startTime;
            }

            int[] var7 = TIMEARR;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                int t = var7[var9];
                if(cost < (long)t) {
                    if(isWriter) {
                        ((AtomicLong)timeStat.wTimeMap.get(Integer.valueOf(t))).incrementAndGet();
                        timeStat.wTotal.incrementAndGet();
                    } else {
                        ((AtomicLong)timeStat.rTimeMap.get(Integer.valueOf(t))).incrementAndGet();
                        timeStat.rTotal.incrementAndGet();
                    }

                    return;
                }
            }

            if(isWriter) {
                timeStat.wTotal.incrementAndGet();
            } else {
                timeStat.rTotal.incrementAndGet();
            }
        } catch (Exception var11) {
            ;
        }

    }

    public static void printStat(int key, TimeStatUtil.TimeStat timestat) {
        String module;
        int port;
        if(key > 4000000) {
            port = key - 4000000;
            if(port > 100000) {
                module = "hbase_batch_";
                port -= 100000;
            } else {
                module = "hbase_";
            }
        } else if(key > 3000000) {
            module = "DB_";
            port = key - 3000000;
        } else if(key > 2000000) {
            port = key - 2000000;
            if(port > 100000) {
                module = "redis_batch_";
                port -= 100000;
            } else {
                module = "redis_";
            }
        } else {
            port = key - 1000000;
            if(port > 100000) {
                module = "memcached_batch_";
                port -= 100000;
            } else {
                if(port <= 0) {
                    return;
                }

                module = "memcached_";
            }
        }

        JSONObject wjb = new JSONObject();
        JSONObject rjb = new JSONObject();
        long wt = timestat.wTotal.get();
        long rt = timestat.rTotal.get();
        wjb.put("name", module + port + "_writer");
        rjb.put("name", module + port + "_read");
        wjb.put("total", wt);
        rjb.put("total", rt);

        for(int i = 0; i < TIMEARR.length; ++i) {
            wjb.put(TIMEARR[i] + "", ((AtomicLong)timestat.wTimeMap.get(Integer.valueOf(TIMEARR[i]))).get());
            rjb.put(TIMEARR[i] + "", ((AtomicLong)timestat.rTimeMap.get(Integer.valueOf(TIMEARR[i]))).get());
        }

        if(wt > 0L) {
            LogCollectorFactory.getLogCollector().log("resourceInterval", module + "writer", wjb.toString());
        }

        if(rt > 0L) {
            LogCollectorFactory.getLogCollector().log("resourceInterval", module + "read", rjb.toString());
        }

    }

    static {
        try {
            printThread.setDaemon(true);
        } catch (Exception var1) {
            ;
        }

        printThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    TimeStatUtil.isStopped = true;
                    TimeStatUtil.printThread.interrupt();
                    TimeStatUtil.printThread.join();
                } catch (InterruptedException var2) {
                    ;
                }

            }
        });
    }

    private static class TimeStat {
        ConcurrentHashMap<Integer, AtomicLong> wTimeMap;
        ConcurrentHashMap<Integer, AtomicLong> rTimeMap;
        AtomicLong wTotal;
        AtomicLong rTotal;

        private TimeStat() {
            this.wTimeMap = new ConcurrentHashMap();
            this.rTimeMap = new ConcurrentHashMap();
            this.wTotal = new AtomicLong(0L);
            this.rTotal = new AtomicLong(0L);
            int[] var1 = TimeStatUtil.TIMEARR;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                int t = var1[var3];
                this.wTimeMap.put(Integer.valueOf(t), new AtomicLong(0L));
                this.rTimeMap.put(Integer.valueOf(t), new AtomicLong(0L));
            }

        }
    }
    
}
