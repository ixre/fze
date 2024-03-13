package net.fze.util.concurrent;

import net.fze.ext.storage.IStorageProvider;
import net.fze.ext.storage.MemoryStorageProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流器
 */
public class TrafficLimiter {
    // 令牌桶
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    // 锁
    //private Object locker = new Object();

    // 桶的容量
    private final long capacity;

    // 令牌放入速度
    private final float rate;

    // 锁定时间
    private final long lockSecond;

    // 数据存储
    private final IStorageProvider store;

    /**
     * 创建限流器, 默认使用内存存储信息
     *
     * @param lockSecond 锁定时间,单位:秒
     * @param capacity   最大容量
     * @param rate       令牌放入速度(每秒放多少个)
     */
    public TrafficLimiter(long capacity, float rate, long lockSecond) {
        this.store = new MemoryStorageProvider();
        this.capacity = capacity;
        this.rate = rate;
        this.lockSecond = lockSecond;
    }

    /**
     * 使用自定义存储创建限流器
     *
     * @param store      存储数据
     * @param lockSecond 锁定时间,单位:秒
     * @param capacity   最大容量
     * @param rate       令牌放入速度(每秒放多少个)
     */
    public TrafficLimiter(IStorageProvider store, long capacity, float rate, long lockSecond) {
        this.store = store;
        this.capacity = capacity;
        this.rate = rate;
        this.lockSecond = lockSecond;
    }

    /**
     * 获取容量
     */
    public long getCapacity() {
        return this.capacity;
    }

    /**
     * 是否锁定
     */
    private boolean isLock(String ip) {
        String k = String.format("_:traffic-limit:%s", ip);
        return this.store.getInt(k) > 0;
    }

    /**
     * 锁定地址
     */
    private void lockTarget(String ip) {
        String k = String.format("_:traffic-limit:%s", ip);
        this.store.setExpire(k, 1, this.lockSecond);
    }

    /**
     * 获取令牌, 如获取不到则被加锁
     *
     * @param n 获取数量
     */
    public boolean acquire(String ip, int n) {
        if (isLock(ip)) {
            return false;
        }
        TokenBucket v;
        if (this.buckets.containsKey(ip)) {
            v = this.buckets.get(ip);
        } else {
            // synchronized(this.locker) {
            v = new TokenBucket(this.capacity, this.rate);
            this.buckets.put(ip, v);
            // }
        }
        if (!v.acquire(n)) {
            this.lockTarget(ip);
            return false;
        }
        return true;
    }

    /**
     * 手动解锁
     */
    public void unlock(String ip) {
        String k = String.format("_:traffic-limit:%s", ip);
        this.store.setExpire(k, 1, 1);
    }
}
