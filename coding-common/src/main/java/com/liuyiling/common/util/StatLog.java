package com.liuyiling.common.util;

import com.alibaba.fastjson.JSONObject;
import com.liuyiling.common.log.LogCollectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 2016/12/21.
 */
public class StatLog implements Runnable{
    private static Logger log = LoggerFactory.getLogger("debug_stat");
    private static AtomicLong count = new AtomicLong(0L);
    private static AtomicLong errorCount = new AtomicLong(0L);
    private static Map<String, AtomicLong> statVars = new ConcurrentHashMap();
    private static Map<String, AtomicLong> lastStatVars = new ConcurrentHashMap();
    private static Map<String, AtomicLong> maxStatVars = new ConcurrentHashMap();
    private static AtomicBoolean outOfMemory = new AtomicBoolean(false);
    private static Map<String, StatLog.ProcessStat> processStats = new ConcurrentHashMap();
    private static Map<String, StatLog.ProcessStat> processStatsLast = new ConcurrentHashMap();
    private static Map<String, StatLog.ProcessStat> processStatsMax = new ConcurrentHashMap();
    private static AtomicBoolean pausePrint = new AtomicBoolean(false);
    private static Map<String, ThreadPoolExecutor> executors = new ConcurrentHashMap();
    private static Set<String> cacheStatKeys = new HashSet();
    private static long startTime;
    private static long interval;
    private boolean isStopped;

    public static void setPausePrint(boolean print) {
        pausePrint.set(print);
    }

    public static long inc() {
        return count.incrementAndGet();
    }

    public static long get() {
        return count.get();
    }

    public static long dec() {
        return count.decrementAndGet();
    }

    public static synchronized void registerVar(String var) {
        if(statVars.get(var) == null) {
            statVars.put(var, new AtomicLong(0L));
            lastStatVars.put(var, new AtomicLong(0L));
            maxStatVars.put(var, new AtomicLong(0L));
        }

    }

    public static void registerExecutor(String name, ThreadPoolExecutor executor) {
        executors.put(name, executor);
    }

    public static long inc(String var) {
        return inc(var, 1);
    }

    public static long inc(String var, int value) {
        AtomicLong c = (AtomicLong)statVars.get(var);
        if(c == null) {
            registerVar(var);
            c = (AtomicLong)statVars.get(var);
        }

        long r = c.addAndGet((long)value);
        if(r < 0L) {
            r = 0L;
            c.set(0L);
        }

        return r;
    }

    public static long dec(String var) {
        AtomicLong c = (AtomicLong)statVars.get(var);
        return c != null?c.decrementAndGet():0L;
    }

    public static long inc(int delta) {
        return count.addAndGet((long)delta);
    }

    public static void incProcessTime(String var, int processCount, long processTime) {
        incProcessTime(processStats, var, processCount, processTime);
        incProcessTime(processStatsLast, var, processCount, processTime);
    }

    private static void incProcessTime(Map<String, StatLog.ProcessStat> pstats, String var, int processCount, long processTime) {
        StatLog.ProcessStat ps = (StatLog.ProcessStat)pstats.get(var);
        if(ps == null) {
            ps = new StatLog.ProcessStat();
            pstats.put(var, ps);
        }

        ps.addStat(processCount, processTime);
    }

    public static long incError() {
        return errorCount.incrementAndGet();
    }

    public static long decError() {
        return errorCount.decrementAndGet();
    }

    public static long getError() {
        return errorCount.get();
    }

    public static long incError(int delta) {
        return errorCount.addAndGet((long)delta);
    }

    public StatLog(long startTime2, long interval2) {
        startTime = startTime2;
        interval = interval2;
    }

    public static void resetStartTime(long newTime) {
        startTime = newTime;
    }

    public static void addCacheStatKeySuffix(String keySuffix) {
        cacheStatKeys.add(keySuffix);
    }

    public static boolean isCacheStatkey(String keySuffix) {
        return keySuffix == null?false:cacheStatKeys.contains(keySuffix);
    }

    public static StatLog printStat(long interval) {
        log.info("Start Api Server stat log.");
        final StatLog statLog = new StatLog(System.currentTimeMillis(), interval);
        final Thread thread = new Thread(statLog, "StatLog");
        thread.setDaemon(true);
        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    statLog.shutdown();
                    thread.interrupt();
                    thread.join();
                } catch (InterruptedException var2) {
                    ;
                }

            }
        });
        return statLog;
    }

    public void shutdown() {
        this.isStopped = true;
    }

    public void run() {
        long lastCount = 0L;
        long cnt = 0L;
        long lastTime = 0L;
        long max = 0L;

        while(!this.isStopped) {
            try {
                synchronized(this) {
                    this.wait(interval);
                }

                if(!pausePrint.get()) {
                    long e = System.currentTimeMillis();
                    if(e == 0L) {
                        break;
                    }

                    if(e != startTime) {
                        cnt = count.get();
                        long cur = (cnt - lastCount) * 1000L / (e - lastTime);
                        if(cur > max) {
                            max = cur;
                        }

                        log.info("---------------------------");
                        log.info("JAVA HEAP: " + memoryReport() + ", UP TIME: " + (e - startTime) / 1000L + ", min: " + (e - startTime) / 60000L);
                        TreeSet keys = new TreeSet(statVars.keySet());
                        StringBuilder sb = new StringBuilder("[");
                        boolean firstLoop = true;
                        Iterator jsonLog = keys.iterator();

                        while(jsonLog.hasNext()) {
                            String i = (String)jsonLog.next();
                            AtomicLong c = (AtomicLong)statVars.get(i);
                            AtomicLong entry = (AtomicLong)lastStatVars.get(i);
                            AtomicLong m1 = (AtomicLong)maxStatVars.get(i);
                            long cnt1 = c.get();
                            if(cnt1 != 0L) {
                                long max1 = m1.get();
                                long lastCount1 = entry.get();
                                long avg1 = cnt1 * 1000L / (e - startTime);
                                long cur1 = (cnt1 - lastCount1) * 1000L / (e - lastTime);
                                if(cur1 > max1) {
                                    max1 = (long)((int)cur1);
                                }

                                if(!firstLoop) {
                                    sb.append(",");
                                } else {
                                    firstLoop = false;
                                }

                                sb.append("{\"").append(i).append("\":[").append(cnt1).append(",").append(avg1).append(",").append(cur1).append(",").append(max1).append("]}\n");
                                m1.set(max1);
                                entry.set(cnt1);
                            }
                        }

                        sb.append("]");
                        log.info(sb.toString());
                        if(processStats.size() > 0) {
                            log.info(this.statProcessSt().toString());
                        }

                        sb.delete(0, sb.length());
                        sb.append("pool:[ ");
                        StringBuilder var33 = new StringBuilder();
                        var33.append("[");
                        int var34 = 0;

                        Map.Entry var36;
                        for(Iterator var35 = executors.entrySet().iterator(); var35.hasNext(); var33.append(this.statJsonExecutor((String)var36.getKey(), (ThreadPoolExecutor)var36.getValue()))) {
                            var36 = (Map.Entry)var35.next();
                            sb.append(this.statExecutor((String)var36.getKey(), (ThreadPoolExecutor)var36.getValue())).append(", ");
                            if(var34++ > 0) {
                                var33.append(",");
                            }
                        }

                        var33.append("]");
                        log.info(sb.append(" ]").toString());
                        LogCollectorFactory.getLogCollector().log("common-pool", "threadpool", var33.toString());
                        lastTime = e;
                        lastCount = cnt;
                    }
                }
            } catch (InterruptedException var32) {
                var32.printStackTrace();
            }
        }

        log.info("Stat log stop");
    }

    private StringBuilder statProcessSt() {
        StringBuilder pstatSb = (new StringBuilder(processStats.size() * 64)).append("processStat: ");

        String psKey;
        for(Iterator var2 = processStats.entrySet().iterator(); var2.hasNext(); processStatsLast.put(psKey, new StatLog.ProcessStat())) {
            Map.Entry entry = (Map.Entry)var2.next();
            psKey = (String)entry.getKey();
            StatLog.ProcessStat ps = (StatLog.ProcessStat)entry.getValue();
            StatLog.ProcessStat psLast = (StatLog.ProcessStat)processStatsLast.get(psKey);
            StatLog.ProcessStat psMax = (StatLog.ProcessStat)processStatsMax.get(psKey);
            if(psMax == null || psMax.getAvgTime() < psLast.getAvgTime()) {
                processStatsMax.put(psKey, psLast);
                psMax = (StatLog.ProcessStat)processStatsMax.get(psKey);
            }

            if(ps.getAvgTime() > 0L) {
                pstatSb.append((String)entry.getKey()).append("{").append(ps.getCount()).append("=").append(ps.getAvgTime()).append(",").append(psLast.getCount()).append("=").append(psLast.getAvgTime()).append(",").append(psMax.getCount()).append("=").append(psMax.getAvgTime()).append("},\n ");
            }
        }

        if(pstatSb.lastIndexOf(",") > 0) {
            pstatSb.delete(pstatSb.lastIndexOf(","), pstatSb.length() - 1);
        }

        return pstatSb;
    }

    public static String memoryReport() {
        Runtime runtime = Runtime.getRuntime();
        double freeMemory = (double)runtime.freeMemory() / 1048576.0D;
        double maxMemory = (double)runtime.maxMemory() / 1048576.0D;
        double totalMemory = (double)runtime.totalMemory() / 1048576.0D;
        double usedMemory = totalMemory - freeMemory;
        double percentFree = (maxMemory - usedMemory) / maxMemory * 100.0D;
        if(percentFree < 10.0D) {
            outOfMemory.set(true);
            log.error("Detected OutOfMemory potentia memory > 90%, stop broadcast presence !!!!!!");
        } else if(outOfMemory.get() && percentFree > 20.0D) {
            outOfMemory.set(false);
            log.error("Detected memory return to normal, memory < 80%, resume broadcast presence.");
        }

        double percentUsed = 100.0D - percentFree;
        DecimalFormat mbFormat = new DecimalFormat("#0.00");
        DecimalFormat percentFormat = new DecimalFormat("#0.0");
        StringBuilder sb = new StringBuilder(" ");
        sb.append(mbFormat.format(usedMemory)).append("MB of ").append(mbFormat.format(maxMemory)).append(" MB (").append(percentFormat.format(percentUsed)).append("%) used");
        return sb.toString();
    }

    public static boolean isOutOfMemory() {
        return outOfMemory.get();
    }

    private String statExecutor(String name, ThreadPoolExecutor executor) {
        StringBuilder strBuf = new StringBuilder(32);
        strBuf.append(name).append("{").append(executor.getQueue().size()).append(",").append(executor.getCompletedTaskCount()).append(",").append(executor.getTaskCount()).append(",").append(executor.getActiveCount()).append(",").append(executor.getCorePoolSize()).append("}\n");
        return strBuf.toString();
    }

    private String statJsonExecutor(String name, ThreadPoolExecutor executor) {
        JSONObject jb = new JSONObject();
        jb.put("name", name);
        jb.put("act", executor.getActiveCount());
        jb.put("core", executor.getCorePoolSize());
        jb.put("max", executor.getMaximumPoolSize());
        return jb.toString();
    }

    static {
        printStat(5000L);
    }

    public static class ProcessStat {
        public AtomicLong count = new AtomicLong();
        public AtomicLong time = new AtomicLong();

        public ProcessStat() {
        }

        private void addStat(int pcount, long ptime) {
            this.count.addAndGet((long)pcount);
            this.time.addAndGet(ptime);
        }

        private long getCount() {
            return this.count.get();
        }

        private long getAvgTime() {
            return this.count.get() > 0L?this.time.get() / this.count.get():0L;
        }
    }
}
