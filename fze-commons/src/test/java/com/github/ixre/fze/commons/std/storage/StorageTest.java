package com.github.ixre.fze.commons.std.storage;

import com.github.ixre.fze.commons.std.Result;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.junit.jupiter.api.Assertions.fail;

public class StorageTest {

    Storage getStorage() {
        String host = "localhost";
        int port = 6379;
        String pwd = "";
        int database = 1;
        int timeout = 0;
        int idle = 100;
        Boolean ssl = false; // r.getBoolean("redis.ssl_enable");
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxIdle(idle);
        if (pwd != null && pwd.equals("")) pwd = null;
        JedisPool pool = new JedisPool(conf, host, port, timeout, pwd, database, ssl);
        return RedisStorage.create(pool);
    }

    @Test
    public void testGet() {
        Storage s = getStorage();
        String v = "1234";
        s.set("key1", v);
        String dv = s.getString("key1");
        if (!dv.equals(v)) {
            fail("GetString失败,origin=1234;now=" + dv);
        }
        Result msg = Result.create(1, "抱歉失败了");
        s.setExpire("key1", msg, 60);
        Result dst = s.get("key1", Result.class);
        if (dst.getErrCode() != msg.getErrCode()) {
            fail("获取对象失败");
        }
    }
}