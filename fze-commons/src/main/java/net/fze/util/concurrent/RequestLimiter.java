package net.fze.util.concurrent;

import net.fze.ext.storage.IStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 请求限制
public class RequestLimiter {
    private Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    // 锁
    //private Object locker = new Object();
    
    // 桶的容量
    private long capacity;

    // 令牌放入速度
    private float rate = 0F;

    // 锁定时间
    private long lockSecond;

    // 数据存储
    private IStorage store;

    // 创建请求限制, store存储数据,lockSecond锁定时间,单位:秒,capacity: 最大容量,rate: 令牌放入速度(每秒放多少个)
    public RequestLimiter(IStorage store, long capacity, float rate, long lockSecond) {
        this.store = store;
        this.capacity = capacity;
        this.rate = rate;
        this.lockSecond = lockSecond;
    }

    /** 获取容量 */
    public long getCapacity() {
        return this.capacity;
    }

    /** 是否锁定 */
    private boolean isLock(String addr) {
        String k = String.format("_:request-limit:%s", addr);
        return this.store.getInt(k) > 0;
    }

    /** 锁定地址 */
    private void lockAddr(String addr) {
        String k = String.format("_:request-limit:%s", addr);
        this.store.setExpire(k, 1, this.lockSecond);
    }

    /** 获取令牌, 如获取不到则被加锁 */
    public boolean acquire(String addr, int n) {
        if (isLock(addr))
            return false;
        TokenBucket v;
        if (this.buckets.containsKey(addr)) {
            v = this.buckets.get(addr);
        } else {
            // synchronized(this.locker) {
            v = new TokenBucket(this.capacity, this.rate);
            this.buckets.put(addr, v);
            // }
        }
        if (!v.acquire(n)) {
            this.lockAddr(addr);
            return false;
        }
        return true;
    }

    /** 解锁 */
    public void unlock(String addr) {
        String k = String.format("_:request-limit:%s", addr);
        this.store.setExpire(k, 1, 1);
    }
}
