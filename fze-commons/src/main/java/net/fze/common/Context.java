package net.fze.common;

import net.fze.lib.injector.Injector;
import net.fze.lib.jdbc.ConnectorManager;
import net.fze.lib.storage.Storage;
import redis.clients.jedis.JedisPool;

public interface Context {
    /**
     * 依赖注入器
     *
     * @return 注入器
     */
    Injector injector();

    /**
     * 数据仓库
     *
     * @return 数据
     */
    Registry registry();

    /**
     * 返回JDBC连接管理器
     *
     * @return
     */
    ConnectorManager jdbc();

    /**
     * 获取Redis实例
     *
     * @return redis
     */
    JedisPool redis();

    /**
     * 存储
     */
    Storage storage();
}
