package com.liuyiling.coding.redis;

import com.liuyiling.coding.redis.client.RedisClient;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 管理某个具体端口(例:6379)的一组多从模式的redis集群
 * 该集群遵循以下原则:
 * 1.写数据时仅写入master,读数据时优先从slave读,slave不可用读时根据具体的配置决定是否从master中读
 * 2.该集群中至少需要有一个master,任意个slave
 * 3.提供getFromMaster等从master直接读数据的方法,用于master-slave同步延迟无法适应的cas场景,例如get,process,set
 *
 * Created by liuyl on 2016/12/20.
 */
public class RedisMSServer {

    //复制key哈希值在该范围内的读写处理
    private long hashMin = -1L;
    private long hashMax = -1L;
    private RedisClient master; //一个master
    private List<RedisClient> slave;   //任意个slave

    private boolean readFromMasterIfSlaveDead = false;

    private ThreadLocal<Random> threadLocalRandom = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random();
        }
    };

    /**
     * 用于spring配置时初始化的方法
     * spring先初始化bean,再按照set注入,最后调用该初始化方法
     * 所以init方法被调用时,master应该是已经被set的
     */
    public void init() {
        if (this.master == null) {
            throw new RuntimeException("init RedisMSServer error,must contain a master server");
        }
        this.master.setThrowJedisException(true);
    }

    /**
     * 为"读"操作从该端口的集群中选择一台机器
     */
    public RedisClient chooseNodeForRead() {

        List<RedisClient> aliveSlaves = new ArrayList<>();
        for (RedisClient redisClient : slave) {
            if(redisClient.isHealthy()){
                aliveSlaves.add(redisClient);
            }
        }

        if(aliveSlaves.size() == 0){
            if(!readFromMasterIfSlaveDead){
                throw new JedisException("no alive redis node slave:" + slave + " ,readFormMasterIfSlaveDead=" + readFromMasterIfSlaveDead);
            } else {
                return master;
            }
        }

        //随机选取可读节点中的某一台用于读数据
        return aliveSlaves.get(threadLocalRandom.get().nextInt(aliveSlaves.size()));
    }

    /**
     * 检查配置id是否应该落在该端口的集群之上
     */
    public boolean contains(final long id) {
        if (hashMin >= 0 && id < hashMin) {
            return false;
        }
        if (hashMax >= 0 && id >= hashMax) {
            return false;
        }
        return true;
    }

    /**
     * 关闭该集群的连接
     */
    public synchronized void close(){
        if(this.master != null){
            this.master.close();
        }
        if(this.slave != null){
            for(RedisClient slave : this.slave){
                slave.close();
            }
        }
    }

    @Override
    public String toString() {
        return "RedisMSServer{" +
                "hashMin=" + hashMin +
                ", hashMax=" + hashMax +
                ", master=" + master +
                ", slave=" + slave +
                '}';
    }


    public void setHashMin(long hashMin) {
        this.hashMin = hashMin;
    }

    public void setHashMax(long hashMax) {
        this.hashMax = hashMax;
    }

    public RedisClient getMaster() {
        return master;
    }

    public void setMaster(RedisClient master) {
        this.master = master;
    }

    public List<RedisClient> getSlave() {
        return slave;
    }

    public void setSlave(List<RedisClient> slave) {
        this.slave = slave;
    }
}
