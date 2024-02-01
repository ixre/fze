package net.fze.mock;

import com.google.inject.Singleton;
import net.fze.common.Registry;
import net.fze.ext.injector.Injector;
import net.fze.ext.jdbc.IDataSourceManager;
import net.fze.ext.storage.RedisStorage;
import net.fze.ext.storage.IStorage;
import redis.clients.jedis.JedisPool;

/**
 * 上下文实现
 */
@Singleton
public class MockContextImpl implements Context {
    private Injector _injector;
    private IStorage _storage;

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
    public IDataSourceManager jdbc() {
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
    public IStorage storage() {
        if (this._storage == null) {
            this._storage = RedisStorage.create(MockApp.getRedisPool());
        }
        return this._storage;
    }
}
