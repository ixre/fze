package net.fze.util.concurrent;

/**
 * 锁
 */
public interface ILocker {
    /**
     * 同步加锁,会阻塞直到获取到锁为止
     *
     * @param key     锁键
     * @param maxLife 最长存活时间(秒),0为永久
     * @return 返回锁id,
     */
    String syncLock(String key, int maxLife);

    /**
     * 互斥锁,如果拿不到锁,直接返回空
     *
     * @param key     锁键
     * @param maxLife 最长存活时间(秒),0为永久
     * @return 返回锁id,
     */
    String mutexLock(String key, int maxLife);

    /**
     * unlock 解锁
     *
     * @param key    锁键
     * @param lockId 锁id
     */
    void unlock(String key, String lockId);
}
