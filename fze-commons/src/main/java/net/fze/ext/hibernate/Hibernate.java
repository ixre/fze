package net.fze.ext.hibernate;

import net.fze.ext.jdbc.ConnectionParams;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;

import java.net.URLEncoder;
import java.util.Properties;

/**
 * Hibernate工具类
 */
public class Hibernate {
    private static final Object _locker = new Object();
    private static SessionFactory _sessionFactory;
    private static String _driverClass;
    private static String _driverUrl;
    private static String _dbPwd;
    private static String _dbUser;
    private static Properties selfSettings;
    private static boolean _cache;
    private static IConfigurationProvider _provider;

    /**
     * 启用hibernate缓存
     */
    public static void enableCache() {
        _cache = true;
    }
//    static boolean getCacheable() {
//        return false;
//    }

    /**
     * 配置Hibernate
     */
    public static void configure(ConnectionParams r, Properties settings) {
         configure(r, settings, null);
    }

    /**
     * 配置Hibernate
     */
    public static void configure(ConnectionParams r, Properties settings,IConfigurationProvider provider) {
//        _driverClass = r.getString("database.driver_class");
//        _driverUrl = String.format(
//                        "jdbc:%s://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
//                        r.getString("database.driver_name"),
//                        r.getString("database.host"),
//                        r.getLong("database.port"),
//                        r.getString("database.db"));
//        _dbUser = r.getString("database.user");
//        _dbPwd = r.getString("database.pwd");
//        String timeZone = r.getString("database.time_zone");
        _driverClass = r.getDriverClass();
        _driverUrl = r.getConnectionUrl();
        _dbPwd = r.getPwd();
        _dbUser = r.getUser();
        if (!r.getTimeZone().isEmpty()) {
            try {
                _driverUrl += "&serverTimezone=" + URLEncoder.encode(r.getTimeZone(), "utf-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        selfSettings = settings;
        _provider = provider;
        // System.out.println("[ Log] Driver Url = " + _driverUrl);
    }

    /**
     * 获取session
     *
     * @return 获取回话
     */
    public static Session getSession() {
        if (_sessionFactory == null) {
            synchronized (_locker) {
                _sessionFactory = createSessionFactory(_provider);
            }
        }
        return _sessionFactory.openSession();
    }

    public static TinySession session() {
        return new SessionImpl(getSession(), _cache);
    }

    /**
     * 创建会话工厂
     *
     * @return 返回工厂
     */
    private static SessionFactory createSessionFactory(IConfigurationProvider supplier) {
//        if (path == null || path.isEmpty()) {
//            throw new Error("[ System][ Crash] - not set hibernate.cfg.xml path for Hibernate");
//        }

        Configuration configuration = new Configuration();
        if(supplier != null)supplier.apply(configuration);
//        for (String pkgPath : _scanPackages) {
//            Arrays.stream(Systems.getPkgClasses(pkgPath,
//                            (c) -> c.isAnnotationPresent(Entity.class)))
//                    .forEach(configuration::addClass);
//        }
        // 读取配置文件信息
        final StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .configure("hibernate.cfg.xml");
        //  .configure(path);
        builder.applySetting("hibernate.connection.driver_class", _driverClass);
        builder.applySetting("hibernate.connection.url", _driverUrl);
        builder.applySetting("hibernate.connection.username", _dbUser);
        builder.applySetting("hibernate.connection.password", _dbPwd);
        builder.applySetting("hibernate.connection.autoReconnect", true);
        builder.applySetting("hibernate.connection.autoReconnectForPools", true);
        builder.applySetting("hibernate.connection.is-connection-validation-required", true);
        // 配置二级缓存
        builder.applySetting("hibernate.cache.use_second_level_cache", "false");
        builder.applySetting("hibernate.cache.use_query_cache", "false");
        builder.applySetting("hibernate.cache.provider_configuration_file_resource_path", "ehcache.xml");
        builder.applySetting("hibernate.cache.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        //builder.applySetting("hibernate.cache.provider_class","ehcache.xml");

        // 配置JTA事务
        builder.applySetting("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        builder.applySetting("javax.persistence.transactionType", "JTA");
        // 配置连接池
        builder.applySetting("hibernate.c3p0.max_statements", 0);
        builder.applySetting("hibernate.c3p0.acquire_increment", 3);
        builder.applySetting("hibernate.c3p0.testConnectionOnCheckin", true);
        builder.applySetting("hibernate.c3p0.testConnectionOnCheckout", false);
        builder.applySetting("hibernate.c3p0.idle_test_period", 3600);
        appendCustomSetting(builder, selfSettings);
        StandardServiceRegistry registry = builder.build();
        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    private static void appendCustomSetting(
            StandardServiceRegistryBuilder builder, Properties properties) throws NullPointerException {
        if (properties != null) {
            for (Object key : properties.keySet()) {
                if (!key.toString().startsWith("hibernate.")) {
                    System.out.println("[ Hibernate][ Configure][ Warning]: unknown setting :" + key);
                } else {
                    Object v = properties.get(key);
                    if (v == null) {
                        throw new NullPointerException("[ Hibernate][ Configure]: settings " + key + " value is null");
                    }
                    builder.applySetting(key.toString(), v);
                }
            }
        }
    }

}
