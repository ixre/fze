package net.fze.util

import net.fze.ext.storage.RedisStorage
import net.fze.ext.storage.Storage
import net.fze.util.concurrent.RequestLimiter
import org.junit.jupiter.api.Test
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

internal class RequestLimiterTest {
    private fun getStorage(): Storage {
        val host = "127.0.0.1"
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
        val rl = RequestLimiter(storage, 30, 5F, 10)
        val ip = "172.17.0.1"
        while (true) {
            for (i in 0..100) {
               if(!rl.acquire(ip, 1)){
                   println("ip locked,please try later")
                   Thread.sleep(1000)
                   continue
               }
               if( i % 10 == 0){
                   //Thread.sleep(1000);
               }
               println("--- Req:${i} => true")
            }
            Thread.sleep(3000)
        }
    }
}
