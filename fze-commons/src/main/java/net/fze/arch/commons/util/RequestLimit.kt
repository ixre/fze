package net.fze.arch.commons.util

import net.fze.arch.commons.std.storage.Storage
import net.fze.arch.commons.util.concurrent.TokenBucket

// 请求限制
class RequestLimit {
   private var buckets : MutableMap<String, TokenBucket> = mutableMapOf()
    // 桶的容量
   private var capacity: Long = 0
    // 令牌放入速度
    private var rate: Float = 0F
    // 锁定时间
    private var lockSecond:Int = 0
    // 锁
    private val locker:Any = Any()
    // 数据存储
    private var store: Storage

    // 创建请求限制, store存储数据,lockSecond锁定时间,单位:秒,capacity: 最大容量,rate: 令牌放入速度
    constructor(store: Storage, capacity: Long, rate: Float, lockSecond:Int){
        this.store = store
        this.capacity = capacity
        this.rate = rate
        this.lockSecond = lockSecond
    }
    // 是否锁定
    fun isLock(addr:String):Boolean{
        val k = String.format("sys:req-limit:%s", addr)
        val v = this.store.getInt(k)
        return  v > 0
    }
    // 锁定地址
    private fun lockAddr(addr:String) {
        val k = String.format("sys:req-limit:%s", addr)
        this.store.setExpire(k, 1, this.lockSecond)
    }

    // 获取令牌
    fun acquire(addr:String, n:Int):Boolean {
        var v : TokenBucket
        if(this.buckets.containsKey(addr)){
            v = this.buckets[addr]!!
        }else{
            synchronized(this.locker){
                v = TokenBucket(this.capacity, this.rate)
                this.buckets.put(addr,v)
            }
        }
        val b = v.acquire(n)
        if(!b){
            this.lockAddr(addr)
        }
        return b
    }
}
