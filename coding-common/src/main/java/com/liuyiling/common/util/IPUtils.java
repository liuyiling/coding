package com.liuyiling.common.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by liuyl on 2016/12/20.
 */
public class IPUtils {

    public static String ipRegix = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static Pattern ipPattern;
    private static long[][] intranet_ip_ranges;
    private static String localIp;
    private static final int MIN_USER_PORT_NUMBER = 1024;
    private static final int MAX_USER_PORT_NUMBER = 65536;

    public IPUtils() {
    }

    public static boolean isIp(String in) {
        return in == null?false:ipPattern.matcher(in).matches();
    }

    public static int ipToInt(String addr, boolean isSegment) {
        String[] addressBytes = addr.split("\\.");
        int length = addressBytes.length;
        if(length < 3) {
            return 0;
        } else {
            int ip = 0;

            try {
                for(int e = 0; e < 3; ++e) {
                    ip <<= 8;
                    ip |= Integer.parseInt(addressBytes[e]);
                }

                ip <<= 8;
                if(!isSegment && length != 3) {
                    ip |= Integer.parseInt(addressBytes[3]);
                } else {
                    ip |= 0;
                }
            } catch (Exception var6) {
                UniversalLogger.warn("Warn ipToInt addr is wrong: addr=" + addr);
            }

            return ip;
        }
    }

    public static int ipToInt(String addr) {
        return ipToInt(addr, false);
    }

    public static boolean isIntranetIP(String ip) {
        if(!isIp(ip)) {
            return false;
        } else {
            long ipNum = (long)ipToInt(ip);
            long[][] var3 = intranet_ip_ranges;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                long[] range = var3[var5];
                if(ipNum >= range[0] && ipNum <= range[1]) {
                    return true;
                }
            }

            return false;
        }
    }

    public static Map<String, String> getLocalIps() {
        try {
            HashMap e = new HashMap();

            String name;
            String ip;
            for(Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces(); netInterfaces.hasMoreElements(); e.put(name, ip)) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                name = ni.getName();
                ip = "";
                Enumeration ips = ni.getInetAddresses();

                while(ips.hasMoreElements()) {
                    InetAddress address = (InetAddress)ips.nextElement();
                    if(address instanceof Inet4Address) {
                        ip = address.getHostAddress();
                        break;
                    }
                }
            }

            return e;
        } catch (SocketException var7) {
            UniversalLogger.error("getLocalIP error", var7);
            return Collections.emptyMap();
        }
    }

    public static String getLocalIp() {
        Map ips = getLocalIps();
        ArrayList faceNames = new ArrayList(ips.keySet());
        Collections.sort(faceNames);
        Iterator var2 = faceNames.iterator();

        while(var2.hasNext()) {
            String name = (String)var2.next();
            if(!"lo".equals(name)) {
                String ip = (String)ips.get(name);
                if(!StringUtils.isBlank(ip)) {
                    return ip;
                }
            }
        }

        return "127.0.0.1";
    }

    public static String getSingleLocalIp() {
        if(localIp == null) {
            localIp = getLocalIp();
        }

        return localIp;
    }

    public static int ramdomAvailablePort() {
        boolean port = false;

        int port1;
        do {
            port1 = (int)(64512.0D * Math.random()) + 1024;
        } while(!availablePort(port1));

        return port1;
    }

    public static boolean availablePort(int port) {
        if(port >= 0 && port <= 65536) {
            ServerSocket ss = null;
            DatagramSocket ds = null;

            try {
                ss = new ServerSocket(port);
                ss.setReuseAddress(true);
                ds = new DatagramSocket(port);
                ds.setReuseAddress(true);
                boolean var3 = true;
                return var3;
            } catch (IOException var13) {
                ;
            } finally {
                if(ds != null) {
                    ds.close();
                }

                if(ss != null) {
                    try {
                        ss.close();
                    } catch (IOException var12) {
                        ;
                    }
                }

            }

            return false;
        } else {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        if(request == null) {
            UniversalLogger.error("GetRealIP getIpAddress request is null");
            return "";
        } else {
            String ip = request.getHeader("X-Real-IP");
            if((ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) && (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("X-Forwarded-For");
            }

            if(ip == null) {
                return "";
            } else {
                String[] ips = ip.split(",");

                for(int index = 0; index < ips.length; ++index) {
                    String strIp = ips[index];
                    if(!"unknown".equalsIgnoreCase(strIp)) {
                        ip = strIp;
                        break;
                    }
                }

                return ip == null?"":ip;
            }
        }
    }

    static {
        ipPattern = Pattern.compile(ipRegix);
        intranet_ip_ranges = new long[][]{{(long)ipToInt("10.0.0.0"), (long)ipToInt("10.255.255.255")}, {(long)ipToInt("172.16.0.0"), (long)ipToInt("172.31.255.255")}, {(long)ipToInt("192.168.0.0"), (long)ipToInt("192.168.255.255")}};
        localIp = null;
    }

}
