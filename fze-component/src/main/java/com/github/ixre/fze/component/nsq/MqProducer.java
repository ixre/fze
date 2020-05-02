package com.github.ixre.fze.commons.nsq;

import com.github.brainlag.nsq.NSQProducer;

/**
 * 生产者
 */
public class MqProducer {
    private static NSQProducer producer;
    private static String _address = "dbs.dev.meizhuli.net";
    private static Integer _port = 4150;

    /**
     * 获取生产者单例
     */
    public static NSQProducer singleton() {
        if (producer == null) {
            if (_address.equals("") || _port <= 0) {
                throw new Error("errMsg queue host or port error");
            }
            producer = new NSQProducer().addAddress(_address, _port).start();
        }
        return producer;
    }

    /**
     * 设置
     *
     * @param host 主机
     * @param port 端口
     */
    public static void configure(String host, Integer port) {
        _address = host;
        _port = port;
    }
}
