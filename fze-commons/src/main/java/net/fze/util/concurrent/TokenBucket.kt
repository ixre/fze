package net.fze.util.concurrent

import net.fze.util.Times

// 令牌桶算法, 参见: https://github.com/ixre/gof/blob/master/util/concurrent/token_bucket.go
class TokenBucket {
    // 时间
    private var timestamp: Int = 0

    // 桶的容量
    var capacity: Long = 0

    // 令牌放入速度
    private var rate: Float = 0F

    // 当前令牌数量
    var tokens: Long = 0

    // capacity 令牌桶的容量; rate 放入令牌桶的速度/每秒放入令牌的数量
    constructor(capacity: Long, rate: Float) {
        this.capacity = capacity
        this.rate = rate
        this.tokens = capacity
        this.timestamp = 0
    }

    // 获取n个令牌
    fun acquire(n: Int): Boolean {
        val now = Times.unix()
        val s = ((now - this.timestamp).toDouble() * this.rate).toInt()
        // 计算当前的令牌数
        this.tokens = this.tokens + s
        // 保存上次请求的时间戳
        this.timestamp = now
        // 如果令牌超出令牌桶的容量
        if (this.tokens > this.capacity)this.tokens = this.capacity
        // 获取令牌,并减去令牌数
        if (this.tokens > n) {
            this.tokens -= n
            return true
        }
        return false
    }
}
