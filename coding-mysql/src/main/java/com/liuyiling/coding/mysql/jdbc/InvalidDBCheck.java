package com.liuyiling.coding.mysql.jdbc;

import com.liuyiling.common.util.UniversalLogger;
import org.apache.mina.util.ConcurrentHashSet;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * auto check db status 
 * set in dead list when this datasource is invalid 
 * remove datasource from dead list when it is restored
 * 
 */
public class InvalidDBCheck {
	
	private static class DBTimeStat{
		AtomicLong total = new AtomicLong(0);
		AtomicLong success = new AtomicLong(0);
	}
	
	public static ConcurrentHashMap<DataSource, DBTimeStat> map = new ConcurrentHashMap<DataSource, DBTimeStat>();//success rate of all dataSources
	private static ConcurrentHashSet<DataSource> deadSet = new ConcurrentHashSet<DataSource>();//dead dataSource list
	private final static int SUCCESS_RATE = 40;//success rate 
	private final static long CHECK_TIME = 1000 * 60;
	private static boolean switcherOpen = false;
	
	private static Thread checkDBThread;
	private static Thread invalidCheckThread;
	
	static{
		checkDBThread = new Thread("checkDBThread"){
			@Override
			public void run() {
				while(true){
					checkStatus();
					try {
						Thread.sleep(CHECK_TIME);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		//将线程设置为守护线程
		try {
			checkDBThread.setDaemon(true);
		} catch (Exception e) {
		}
		checkDBThread.start();

		invalidCheckThread = new Thread("invalidCheckThread"){
			@Override
			public void run() {
				while(true){
					invalidCheckDead();
					try {
						Thread.sleep(CHECK_TIME);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		//将线程设置为守护线程
		try {
			invalidCheckThread.setDaemon(true);
		} catch (Exception e) {
		}
		invalidCheckThread.start();
	}
	
	public static void checkStatus(){
		long total;
		long success;
		for(DataSource ds : map.keySet()){
			try {
				DBTimeStat stat = map.get(ds);
				total = stat.total.get();
				success = stat.success.get();
				//为防止网络抖动对服务的影响，对周期内请求量少的端口做增大周期判断处理
				if(total >20){
					map.put(ds, new DBTimeStat());
					if(success * 100.00 / total < SUCCESS_RATE){
						deadSet.add(ds);
						UniversalLogger.info("Invalid add dead: " + ((com.mchange.v2.c3p0.ComboPooledDataSource) ds).getJdbcUrl() + ", success:" + success + ",total:" + total);
					}
				}
			} catch (Exception e) {}
		}
	}
	
	public static void invalidCheckDead(){
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		long start = 0;
		long time = 0;
		for(DataSource ds : deadSet){
			try {
				start = System.currentTimeMillis();
				con = DataSourceUtils.getConnection(ds);
				stmt = con.createStatement();
				String sql = "select 12345";
				result = stmt.executeQuery(sql);
				time = System.currentTimeMillis() - start;
				if(result != null && time <500){
					deadSet.remove(ds);
					UniversalLogger.info("Invalid remove dead: "+((com.mchange.v2.c3p0.ComboPooledDataSource)ds).getJdbcUrl());
				}
			}catch(SQLException e){
			}catch (Exception e1) {
			}finally{
				//release connection
				try {
					if(result != null){
						result.close();
					}
				} catch (Exception e) {
				}
				JdbcUtils.closeStatement(stmt);
				DataSourceUtils.releaseConnection(con, ds);
			}
		}
	}

	public static void register(DataSource ds){
		if(map.get(ds) == null){
			UniversalLogger.info("Invalid register: "+((com.mchange.v2.c3p0.ComboPooledDataSource)ds).getJdbcUrl());
			map.putIfAbsent(ds, new DBTimeStat());
		}
	}
	
	private static DBTimeStat getDBTimeStat(DataSource ds){
		DBTimeStat stat = map.get(ds);
		if(stat == null){
			register(ds);
			stat = map.get(ds);
		}
		return stat;
	}
	
	public static void addElapseDBTimeStat(DataSource ds, boolean success, long time, long fireTime){
		try {
			//增加开关，当开关关闭后后续所有工作不做
			if(!switcherOpen){
				return ;
			}
			
			DBTimeStat stat = getDBTimeStat(ds);
			stat.total.incrementAndGet();
			boolean suc = success && time < fireTime;
			if(suc){
				stat.success.incrementAndGet();
			}
			if(!suc){
			}
		} catch (Exception e) {
			UniversalLogger.info("Invalid add error: "+e.getMessage());
		}
	}
	
	public static void setSwitcher(boolean isOpen){
		switcherOpen = isOpen;
		//开关关闭后清空所有数据，不做健康检测，也不做恢复探测
		if(!switcherOpen){
			map.clear();
			deadSet.clear();
		}
	}

	public static boolean isInvalidDB(DataSource ds){
		return deadSet.contains(ds);
	}
	
}
