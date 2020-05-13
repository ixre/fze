package net.fze.commons.libs.observer;
/*
 created for report-server [ RedisObserver.java ]
 user: liuming (jarrysix@gmail.com)
 date: 28/12/2017 17:23
 description:
*/

import net.fze.commons.std.LangExtension;
import net.fze.commons.std.LangExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REDIS队列监控
 */
public class RedisObserver implements IObserverProvider {
    private JedisPool pool;
    private Map<String, IObserver> observerMap = new HashMap<>();

    public RedisObserver(JedisPool pool) {
        this.pool = pool;
    }

    public void register(String key, IObserver obs) {
        this.observerMap.put(key, obs);
    }

    /**
     * 运行队列
     */
    public void observe() {
        if (this.observerMap.size() == 0) {
            throw new Error("[ Observer]: no any observer! please register first");
        }
        new LangExtension().threadRun(this::threadRun);
    }

    private void threadRun() {
        Jedis rds = this.pool.getResource();
        while (true) {
            String[] keys = new String[this.observerMap.size()];
            int i = 0;
            for (String key : this.observerMap.keySet()) {
                keys[i++] = key;
            }
            List<String> list = rds.blpop(0, keys);
            String key = list.get(0);
            if (this.observerMap.containsKey(key)) {
                Error err = this.observerMap.get(key).receive(key, list.get(1));
                if (err != null) {
                    this.handleError(key, list.get(1), err);
                }
            }
        }
    }

    /**
     * 记录队列错误
     *
     * @param key 队列键
     * @param val 对列值
     * @param err 错误
     */
    public void handleError(String key, String val, Error err) {
        System.out.println("[ Queue][ Error]: " + err.getMessage() + "; key=" + key + "; value=" + val);
    }
}
