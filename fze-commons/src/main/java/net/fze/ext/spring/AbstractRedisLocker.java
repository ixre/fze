package net.fze.ext.spring;

import net.fze.util.Strings;
import net.fze.util.concurrent.ILocker;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * Redis分步式锁
 * @author jarrysix
 */
public abstract class AbstractRedisLocker implements ILocker {

    private final StringRedisTemplate redisTemplate;

    public AbstractRedisLocker(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 同步加锁,会阻塞直到获取到锁为止
     *
     * @param key     锁键
     * @param maxLife 最长存活时间(秒),0为永久
     * @return 返回锁id,
     */
    @Override
    public String syncLock(String key, int maxLife) {
        String uuid;
        do {
            uuid = this.mutexLock(key, maxLife);
        } while (Strings.isNullOrEmpty(uuid));
        return uuid;
    }

    /**
     * 互斥锁,如果拿不到锁,直接返回空
     *
     * @param key     锁键
     * @param maxLife 最长存活时间(秒),0为永久
     * @return 返回锁id,
     */
    @Override
    public String mutexLock(String key, int maxLife) {
        String uuid = UUID.randomUUID().toString();
        long expiresTime = maxLife * 1000L;
        try {
            boolean result = Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent("_lock_" + key, uuid, expiresTime, TimeUnit.MILLISECONDS));
            return result ? uuid : null;
        } catch (IllegalStateException ex) {
            if (ex.getMessage().contains("destroyed")) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加锁
     *
     * @param key     Key
     * @param expires 过期时间
     * @return 锁id
     */
    public String lock(String key, int expires) {
        return this.mutexLock(key, expires);
    }


    /**
     * 加锁并等待直到获取到锁
     */
    public String lockAndWait(String key, int expires) {
        return this.syncLock(key, expires);
    }

    /**
     * unlock 解锁
     */
    @Override
    public void unlock(String key, String uuid) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("lock key is null");
        }
        if (uuid == null) {
            throw new IllegalArgumentException("lock uuid is null, key=" + key);
        }
        final String unlockScript = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1]) " +
                "else return -1 end";
        redisTemplate.execute((RedisConnection connection) -> connection.eval(
                unlockScript.getBytes(),
                ReturnType.INTEGER,
                1,
                ("_lock_" + key).getBytes(),
                uuid.getBytes()));

    }
}
