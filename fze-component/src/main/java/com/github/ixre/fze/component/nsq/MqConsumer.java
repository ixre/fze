package com.github.ixre.fze.commons.nsq;

import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;

import java.util.HashMap;
import java.util.Map;

public class MqConsumer {
    private static Map<String, Integer> hostAndPorts = new HashMap<>();

    /**
     * 增加发现地址
     *
     * @param host 主机
     * @param port 端口
     */
    public static void addLookupAddr(String host, Integer port) {
        hostAndPorts.put(host, port);
    }

    /**
     * 获取发现地址
     *
     * @return 地址
     */
    public static NSQLookup getLoopup() {
        NSQLookup lookup = new DefaultNSQLookup();
        if (hostAndPorts == null || hostAndPorts.size() == 0) {
            throw new Error("未添加Lookup地址");
        }
        for (Map.Entry<String, Integer> pair : hostAndPorts.entrySet()) {
            lookup.addLookupAddress(pair.getKey(), pair.getValue());
        }
        return lookup;
    }
}
