package com.liuyiling.coding.effective.initial.destory.object;

/**
 * Created by liuyl on 16/1/10.
 * 服务提供者框架有四种角色：
 * 1.服务接口   eg.Connetction
 * 2.服务访问接口 eg.DriverManager.getConnection
 * 3.服务者注册接口    eg.DriverManager.registerDriver
 * 4.服务提供者接口    eg.Driver
 * <p>
 * 适用于：
 * 1.不可变类
 * 2.具有实例缓存的类
 * 3.枚举类（实例的数量有限的类）
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务接口   eg.Connetction
 */
interface Service {
    //主要服务方法
}

/**
 * 服务提供者接口    eg.Driver
 * 负责创造实例
 */
interface Provider {
    Service newService();
}

public class Services {

    private Services() {
    }

    private static final Map<String, Provider> providers =
            new ConcurrentHashMap<>();

    private static final String DEFAULT_PROVIDERNAME = "def";

    //服务者注册接口    eg.DriverManager.registerDriver
    public static void registerProvider(String name, Provider p) {
        providers.put(name, p);
    }

    public static void registerDefaultProvider(Provider p) {
        registerProvider(DEFAULT_PROVIDERNAME, p);
    }


    //服务访问接口 eg.DriverManager.getConnection
    public static Service newInstance(String name) {
        Provider p = providers.get(name);
        if (p == null) {
            throw new IllegalArgumentException(
                    "No provider registered with name: " + name
            );
        }
        return p.newService();
    }

    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDERNAME);
    }
}
