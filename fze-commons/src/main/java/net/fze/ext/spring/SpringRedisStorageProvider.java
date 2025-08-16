package net.fze.ext.spring;

import com.google.gson.Gson;
import net.fze.ext.storage.IStorageProvider;
import net.fze.util.Strings;
import net.fze.util.TypeConv;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis存储实现
 * <p>
 * example:
 * // @Component
 * &#064;Bean
 * public IStorageProvider storageProvider(RedisConnectionFactory redisConnectionFactory){
 * return new SpringRedisStorageProvider(redisConnectionFactory);
 * }
 *
 * @author jarrysix
 */
public class SpringRedisStorageProvider implements IStorageProvider {
    private final StringRedisTemplate redisTemplate;
    private final Gson gson = new Gson();

    public SpringRedisStorageProvider(StringRedisTemplate pool) {
        this.redisTemplate = pool;
    }

    public SpringRedisStorageProvider(RedisConnectionFactory redisConnectionFactory) {
        this.redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    }

    @Override
    public String driver() {
        return "redis-storage";
    }

    @Override
    public Object source() {
        return this.redisTemplate;
    }


    @Override
    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    @Override
    public void set(String key, Object v) {
        this.redisTemplate.opsForValue().set(key, this.getObjectValue(v));
    }

    @Override
    public void setExpire(String key, Object v, long seconds) {
        this.set(key,v);
        this.redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
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
        if (!Strings.isNullOrEmpty(s)) {
            return "true".equals(s) || "1".equals(s);
        }
        return false;
    }

    @Override
    public int getInt(String key) {
        String s = this.getString(key);
        if (!Strings.isNullOrEmpty(s)) {
            return TypeConv.toInt(s);
        }
        return -1;
    }

    @Override
    public long getInt64(String key) {
        String s = this.getString(key);
        if (!Strings.isNullOrEmpty(s)) {
            return TypeConv.toLong(s);
        }
        return -1;
    }

    @Override
    public String getString(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public float getFloat(String key) {
        String s = this.getString(key);
        if (!Strings.isNullOrEmpty(s)) {
            return TypeConv.toFloat(s);
        }
        return -1;
    }

    @Override
    public byte[] getBytes(String key) {
        String value = this.redisTemplate.opsForValue().get(key);
        return value != null ? value.getBytes() : null;
    }

    @Override
    public void remove(String pattern) {
        if (!pattern.contains("*")) {
            this.redisTemplate.delete(pattern);
        } else {
            Set<String> keys = this.redisTemplate.keys(pattern);
            if (!keys.isEmpty()) {
                this.redisTemplate.delete(keys);
            }
        }
    }
}
