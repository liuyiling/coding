package com.liuyiling.java.network;

import java.net.InetAddress;

/**
 * Created by liuyl on 15/12/22.
 * 获取多网卡ip
 */
public class GetLocalHostIP {

    public String getLocalHostName() {
        String hostName;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
        } catch (Exception ex) {
            hostName = "";
        }
        return hostName;
    }

    public String[] getAllLocalHostIP() {
        String[] ret = null;
        try {
            String hostName = getLocalHostName();
            if (hostName.length() > 0) {
                InetAddress[] addrs = InetAddress.getAllByName(hostName);
                if (addrs.length > 0) {
                    ret = new String[addrs.length];
                    for (int i = 0; i < addrs.length; i++) {
                        ret[i] = addrs[i].getHostAddress();
                    }
                }
            }

        } catch (Exception ex) {
            ret = null;
        }
        return ret;
    }

    public static void main(String[] agrs) {

        GetLocalHostIP getLocalHostIP = new GetLocalHostIP();


    }

}
