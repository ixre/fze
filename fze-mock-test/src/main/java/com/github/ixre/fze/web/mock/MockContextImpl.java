package com.github.ixre.fze.web.mock;

import com.google.inject.Singleton;
import com.github.ixre.fze.commons.Context;
import com.github.ixre.fze.commons.Injector;
import com.github.ixre.fze.commons.Registry;
import com.github.ixre.fze.commons.jdbc.ConnectorManager;
import com.github.ixre.fze.commons.std.storage.RedisStorage;
import com.github.ixre.fze.commons.std.storage.Storage;
import redis.clients.jedis.JedisPool;

/**
 * 上下文实现
 */
@Singleton
public class MockContextImpl implements Context {
    private Injector _injector;
    private Storage _storage;

    @Override
    public Injector injector() {
        if (this._injector == null) {
            this._injector = new MockInjector(MockApp.getInjector());
        }
        return this._injector;
    }

    @Override
    public Registry registry() {
        return MockApp.getRegistry();
    }

    /**
     * 返回JDBC连接管理器
     *
     * @return
     */
    @Override
    public ConnectorManager jdbc() {
        return MockApp.getJdbcManager();
    }

    /**
     * 获取Redis实例
     *
     * @return redis
     */
    public JedisPool redis() {
        return MockApp.getRedisPool();
    }

    /**
     * 存储
     */
    @Override
    public Storage storage() {
        if (this._storage == null) {
            this._storage = RedisStorage.create(MockApp.getRedisPool());
        }
        return this._storage;
    }
}
