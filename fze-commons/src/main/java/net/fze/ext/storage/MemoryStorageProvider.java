package net.fze.ext.storage;

import net.fze.util.Times;
import net.fze.util.TypeConv;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于内存实现的存储
 * @author jarrysix
 */
public class MemoryStorageProvider implements IStorageProvider {
    private final HashMap<String, Object> data = new HashMap<>();
    private final HashMap<String, Long> expires = new HashMap<>();

    @Override
    public String driver() {
        return "memory";
    }

    @Override
    public Object source() {
        return this.data;
    }

    @Override
    public boolean exists(String key) {
        return this.data.containsKey(key);
    }

    @Override
    public void set(String key, Object v) {
        this.data.put(key, v);
    }

    @Override
    public void setExpire(String key, Object v, long seconds) {
        long expiresTime = Times.unix() + seconds;
        this.data.put(key, v);
        this.expires.put(key, expiresTime);
    }

    @Override
    public <T> T get(String key, Class<T> c) {
        Object v = this.getRaw(key);
        assert v != null;
        return (T) v;
    }

    @Override
    public Object getRaw(String key) {
        this.clean();
        return this.data.get(key);
    }

    /**
     * 清理已过期
     */
    private void clean() {
        long now = Times.unix();
        this.expires.entrySet().stream()
                .filter(entry -> entry.getValue() < now)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).forEach(k -> {
                    this.data.remove(k);
                    this.expires.remove(k);
                });

    }

    @Override
    public Boolean getBool(String key) {
        return TypeConv.toBoolean(this.getRaw(key));
    }

    @Override
    public int getInt(String key) {
        return TypeConv.toInteger(this.getRaw(key));

    }

    @Override
    public long getInt64(String key) {
        return TypeConv.toLong(this.getRaw(key));
    }

    @Override
    public String getString(String key) {
        return TypeConv.toString(this.getRaw(key));
    }

    @Override
    public float getFloat(String key) {
        return TypeConv.toFloat(this.getRaw(key));
    }

    @Override
    public byte[] getBytes(String key) {
        return new byte[0];
    }

    @Override
    public void remove(String pattern) {
        for (String key : this.data.keySet()) {
            if (key.contains(pattern)) {
                this.data.remove(key);
            }
        }
    }
}
