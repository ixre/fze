package net.fze.mock;

import com.google.inject.Singleton;
import net.fze.common.Context;
import net.fze.libs.injector.Injector;
import net.fze.common.Registry;
import net.fze.libs.jdbc.ConnectorManager;
import net.fze.libs.storage.RedisStorage;
import net.fze.libs.storage.Storage;
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
