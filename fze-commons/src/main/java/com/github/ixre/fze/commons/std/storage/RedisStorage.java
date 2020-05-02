package com.github.ixre.fze.commons.std.storage;

import com.google.gson.Gson;
import com.github.ixre.fze.commons.std.Types;
import com.github.ixre.fze.commons.std.TypesConv;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class RedisStorage implements Storage {
    private final JedisPool pool;
    private Gson gson = new Gson();

    private RedisStorage(JedisPool pool) {
        this.pool = pool;
    }

    public static Storage create(JedisPool pool) {
        return new RedisStorage(pool);
    }

    @Override
    public String driver() {
        return "redis-storage";
    }

    @Override
    public Object source() {
        return this.pool;
    }

    @Override
    public boolean exists(String key) {
        Jedis rds = this.pool.getResource();
        boolean b = rds.exists(key);
        rds.close();
        return b;
    }

    @Override
    public Error set(String key, Object v) {
        Jedis rds = this.pool.getResource();
        rds.set(key, this.getObjectValue(v));
        rds.close();
        return null;
    }

    @Override
    public Error setExpire(String key, Object v, int seconds) {
        Jedis rds = this.pool.getResource();
        rds.setex(key, seconds, this.getObjectValue(v));
        rds.close();
        return null;
    }

    private String getObjectValue(Object v) {
        String pkg = v.getClass().getPackage().getName();
        if (pkg.startsWith("java.lang")) {
            return String.valueOf(v);
        }
        return gson.toJson(v);
    }

    @Override
    public <T> T get(String key, Class<T> c) {
        String s = this.getString(key);
        return gson.fromJson(s, c);
    }

    @Override
    public Object getRaw(String key) {
        throw new Error("Redis Storage不支持getRaw()");
    }

    @Override
    public Boolean getBool(String key) {
        String s = this.getString(key);
        if (!Types.emptyOrNull(s)) {
            return s.equals("true") || s.equals("1");
        }
        return false;
    }

    @Override
    public int getInt(String key) {
        String s = this.getString(key);
        if (!Types.emptyOrNull(s)) {
            return TypesConv.toInt(s);
        }
        return -1;
    }

    @Override
    public long getInt64(String key) {
        String s = this.getString(key);
        if (!Types.emptyOrNull(s)) {
            return TypesConv.toLong(s);
        }
        return -1;
    }

    @Override
    public String getString(String key) {
        Jedis rds = this.pool.getResource();
        String b = rds.get(key);
        rds.close();
        return b;
    }

    @Override
    public float getFloat(String key) {
        String s = this.getString(key);
        if (!Types.emptyOrNull(s)) {
            return TypesConv.toFloat(s);
        }
        return -1;
    }

    @Override
    public byte[] getBytes(String key) {
        Jedis rds = this.pool.getResource();
        byte[] b = rds.get(key.getBytes());
        rds.close();
        return b;
    }

    @Override
    public void del(String pattern) {
        Jedis rds = this.pool.getResource();
        if (pattern.indexOf("*") == -1) {
            rds.del(pattern);
        } else {
            Set<String> keys = rds.keys(pattern);
            if(keys.size() > 0) {
                String[] keyArray = new String[keys.size()];
                rds.del(keys.toArray(keyArray));
            }
        }
        rds.close();
    }
}

