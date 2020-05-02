package com.github.ixre.fze.commons.infrastructure;

import com.github.ixre.fze.commons.Context;
import redis.clients.jedis.Jedis;

public class Queue {
    private Context ctx;

    protected Queue(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 向队列推送信息
     *
     * @param channel 队列
     * @param message 消息
     * @return 结果
     */
    public Long push(String channel, String message) {
        Jedis rds = this.ctx.redis().getResource();
        Long l = rds.rpush(channel, message);
        rds.close();
        return l;
    }
}
