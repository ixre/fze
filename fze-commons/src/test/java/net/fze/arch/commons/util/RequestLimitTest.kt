package net.fze.arch.commons.util

import net.fze.arch.commons.std.storage.RedisStorage
import net.fze.arch.commons.std.storage.Storage
import org.junit.jupiter.api.Test
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

internal class RequestLimitTest {
    internal fun getStorage(): Storage {
        val host = "dbs.dev1.super4bit.co"
        val port = 6379
        var pwd: String? = ""
        val database = 1
        val timeout = 0
        val idle = 100
        val ssl = false // r.getBoolean("redis.ssl_enable");

        val conf = JedisPoolConfig()
        conf.maxIdle = idle
        if (pwd != null && pwd == "") pwd = null
        val pool = JedisPool(conf, host, port, timeout, pwd, database, ssl)
        return RedisStorage.create(pool)
    }

    @Test
    fun acquire() {
        val storage = this.getStorage();
        val rl = RequestLimit(storage, 30, 10F, 10)
        val ip = "172.17.0.1"
        while (true) {
            for (i in 0..100) {
                if (rl.isLock(ip)) {
                    println("ip locked,please try later")
                    Thread.sleep(1000)
                    continue
                }
                val b = rl.acquire(ip, 1)
                println("--- Req:${i} => ${b}")
            }
            Thread.sleep(3000)
        }
    }
}