package net.fze.util.concurrent;

import net.fze.util.Times;
import net.fze.util.TypeConv;

// 令牌桶算法, 参见: https://github.com/ixre/gof/blob/master/util/concurrent/token_bucket.go
public class TokenBucket {
    // 时间
    private long timestamp;
    // 桶的容量
    private long capacity;
    // 令牌放入速度
    private float rate;

    // 当前令牌数量
    private long tokens;

    // capacity 令牌桶的容量; rate 放入令牌桶的速度/每秒放入令牌的数量
    TokenBucket(long capacity, float rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.timestamp = 0;
    }

    // 获取n个令牌
    public boolean acquire(int n) {
        long now = Times.unix();
        // 计算区间应放入的令牌数
        int s = TypeConv.toInt(TypeConv.toDouble(now - this.timestamp) * this.rate);
        // 计算当前的令牌数
        this.tokens = this.tokens + s;
        // 保存上次请求的时间戳
        this.timestamp = now;
        // 如果令牌超出令牌桶的容量
        if (this.tokens > this.capacity)
            this.tokens = this.capacity;
        // 获取令牌,并减去令牌数
        if (this.tokens >= n) {
            this.tokens -= n;
            return true;
        }
        return false;
    }
}
