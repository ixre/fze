package com.github.ixre.fze.commons.util;
/*
  created for mzl-server [ MyUtils.java ]
  user: liuming (jarrysix@gmail.com)
  date: 13/12/2017 14:03
  description: 
 */

import java.net.InetAddress;
import java.net.Socket;

public class MyUtils {
    /**
     * 解析主机名
     *
     * @param ipAndPort IP和端口，如：127.0.0.1:8080
     * @return 默认主机头
     */
    private static String resolveHostName(String ipAndPort) {
        //HostAndPort hp = HostAndPort.fromString(ipAndPort);
        // return hp.getHostText();
        return "";
    }

    public static Boolean ping(String server) {
        try {
            return InetAddress.getByName(server).isReachable(10);
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 检测端口是否开放
     *
     * @param host 主机
     * @param port 端口
     * @return 是否开放
     */
    public static Boolean detectPort(String host, int port) {
        try {
            new Socket(host, port);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 判断ra,rb是否在la,lb区间内
     */
    public static Boolean inRange(int la, int lb, int ra, int rb) {
        return (la >= ra && la < rb) ||
                (la > ra && la <= rb) ||
                (la <= ra && lb > rb) ||
                (la < ra && lb >= ra);
    }
}
