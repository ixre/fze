package net.fze.ext.hibernate;

import net.fze.ext.jdbc.ConnectionParams;
import net.fze.util.Strings;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
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
    private static String _confPath;

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
        configure(r, settings, null, "hibernate.cfg.xml");
    }

    /**
     * 配置Hibernate
     */
    public static void configure(ConnectionParams r, Properties settings, IConfigurationProvider provider, String confPath) {
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
        _confPath = confPath;
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
        if(_driverUrl == null || _driverClass == null){
            throw new IllegalArgumentException("please hibernate configure first");
        }
//        if (path == null || path.isEmpty()) {
//            throw new Error("[ System][ Crash] - not set hibernate.cfg.xml path for Hibernate");
//        }

        Configuration configuration = new Configuration();
        if (supplier != null) supplier.apply(configuration);
//        for (String pkgPath : _scanPackages) {
//            Arrays.stream(Systems.getPkgClasses(pkgPath,
//                            (c) -> c.isAnnotationPresent(Entity.class)))
//                    .forEach(configuration::addClass);
//        }

        // org.hibernate.MappingException: Unknown entity
        // https://blog.csdn.net/weixin_34280237/article/details/91672332 Hibernate4.x

//        if(Strings.isNullOrEmpty(_confPath)) {
//            configuration.configure();
//        }else{
//            configuration.configure("hibernate.cfg.xml");
//        }
        // 读取配置文件信息
//        final StandardServiceRegistryBuilder builder =
//                new StandardServiceRegistryBuilder()
//                        .applySettings(configuration.getProperties());

//
//        //  .configure(path);
//        builder.applySetting("hibernate.connection.driver_class", _driverClass);
//        builder.applySetting("hibernate.connection.url", _driverUrl);
//        builder.applySetting("hibernate.connection.username", _dbUser);
//        builder.applySetting("hibernate.connection.password", _dbPwd);
//        builder.applySetting("hibernate.connection.autoReconnect", true);
//        builder.applySetting("hibernate.connection.autoReconnectForPools", true);
//        builder.applySetting("hibernate.connection.is-connection-validation-required", true);
//        // 配置二级缓存
//        builder.applySetting("hibernate.cache.use_second_level_cache", "false");
//        builder.applySetting("hibernate.cache.use_query_cache", "false");
//        builder.applySetting("hibernate.cache.provider_configuration_file_resource_path", "ehcache.xml");
//        builder.applySetting("hibernate.cache.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
//        //builder.applySetting("hibernate.cache.provider_class","ehcache.xml");
//
//        // 配置JTA事务
//        builder.applySetting("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
//        builder.applySetting("javax.persistence.transactionType", "JTA");
//        // 配置连接池
//        builder.applySetting("hibernate.c3p0.max_statements", 0);
//        builder.applySetting("hibernate.c3p0.acquire_increment", 3);
//        builder.applySetting("hibernate.c3p0.testConnectionOnCheckin", true);
//        builder.applySetting("hibernate.c3p0.testConnectionOnCheckout", false);
//        builder.applySetting("hibernate.c3p0.idle_test_period", 3600);
//        appendCustomSetting(builder, selfSettings);
//        StandardServiceRegistry registry = builder.build();
//        return new MetadataSources(registry)
//                .buildMetadata()
//                .buildSessionFactory();

        // hibernate 5.x
        //configuration.addAnnotatedClass(this.getClass());
        if (Strings.isNullOrEmpty(_confPath)) {
            configuration.configure();
        } else {
            configuration.configure("hibernate.cfg.xml");
        }
        configuration.setProperty("hibernate.connection.driver_class", _driverClass);
        configuration.setProperty("hibernate.connection.url", _driverUrl);
        configuration.setProperty("hibernate.connection.username", _dbUser);
        configuration.setProperty("hibernate.connection.password", _dbPwd);
        configuration.setProperty("hibernate.connection.autoReconnect", "true");
        configuration.setProperty("hibernate.connection.autoReconnectForPools", "true");
        configuration.setProperty("hibernate.connection.is-connection-validation-required", "true");
        // 配置二级缓存
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.cache.use_query_cache", "false");
        configuration.setProperty("hibernate.cache.provider_configuration_file_resource_path", "ehcache.xml");
        configuration.setProperty("hibernate.cache.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        //configuration.setProperty("hibernate.cache.provider_class","ehcache.xml");

        // 配置JTA事务
        configuration.setProperty("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        configuration.setProperty("javax.persistence.transactionType", "JTA");
        // 配置连接池
        configuration.setProperty("hibernate.c3p0.max_statements", "0");
        configuration.setProperty("hibernate.c3p0.acquire_increment", "3");
        configuration.setProperty("hibernate.c3p0.testConnectionOnCheckin", "true");
        configuration.setProperty("hibernate.c3p0.testConnectionOnCheckout", "false");
        configuration.setProperty("hibernate.c3p0.idle_test_period", "3600");
        appendCustomSetting(configuration, selfSettings);
        return configuration.buildSessionFactory();
//        StandardServiceRegistry registry = builder.build();
//        return new MetadataSources(registry)
//                .buildMetadata()
//                .buildSessionFactory();
    }

    private static void appendCustomSetting(
            Configuration builder, Properties properties) throws NullPointerException {
        if (properties != null) {
            for (Object key : properties.keySet()) {
                if (!key.toString().startsWith("hibernate.")) {
                    System.out.println("[ Hibernate][ Configure][ Warning]: unknown setting :" + key);
                } else {
                    Object v = properties.get(key);
                    if (v == null) {
                        throw new NullPointerException("[ Hibernate][ Configure]: settings " + key + " value is null");
                    }
                    builder.setProperty(key.toString(), v.toString());
                }
            }
        }
    }

}
