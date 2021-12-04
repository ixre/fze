package net.fze.mock;

import com.google.inject.Injector;
import net.fze.common.Registry;
import net.fze.lib.jdbc.ConnectorManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用
 */
class MockApp {
    private static Registry _registry;
    private static Injector _injector;
    private static JedisPool _redisPool;

    /**
     * 应用初始化
     *
     * @param confPath 配置目录
     */
    public static void initialize(String confPath) {
        _registry = new Registry(confPath);
        initRedis(_registry);
        // initDb(_registry);
    }

    private static void initDb(Registry registry) {
        Map<String, Object> settings = new HashMap<>();
        // Hibernate.configure(registry.getString("hibernate.location"), registry, settings);
    }

    /**
     * 初始化REDIS
     *
     * @param r 注册表
     */
    private static void initRedis(Registry r) {
        try {
            String host = r.getString("redis.host");
            Long port = r.getLong("redis.port");
            String pwd = r.getString("redis.auth");
            Long database = r.getLong("redis.database");
            JedisPoolConfig conf = new JedisPoolConfig();
            conf.setMaxTotal(60);
            conf.setMaxIdle(20);
            conf.setMaxWaitMillis(5000);
            if (pwd != null && pwd.equals("")) pwd = null;
            _redisPool =
                    new JedisPool(conf, host, port.intValue(), 5000, pwd, database.intValue(), false);
            Jedis rds = _redisPool.getResource();
            String pingResult = rds.ping();
            rds.close();
            if (!pingResult.toLowerCase().equals("pong")) {
                System.out.println("--Redis连接失败" + pingResult);
                System.exit(1);
            }
        } catch (Exception ex) {
            System.out.println("[ Sys][ Crash]: Redis初始化失败!详情：" + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 获取数据字典
     *
     * @return 数据
     */
    public static Registry getRegistry() {
        return _registry;
    }

    /**
     * 获取依赖注入
     *
     * @return -
     */
    public static Injector getInjector() {
        return _injector;
    }

    /**
     * 获取依赖注入
     *
     * @param injector 注入器
     */
    public static void setInjector(Injector injector) {
        _injector = injector;
    }

    public static JedisPool getRedisPool() {
        return _redisPool;
    }

    public static ConnectorManager getJdbcManager() {
        throw new Error("不支持JDBC连接");
    }
}
