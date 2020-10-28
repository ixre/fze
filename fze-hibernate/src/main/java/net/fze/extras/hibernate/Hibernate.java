package net.fze.extras.hibernate;

import net.fze.common.Registry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Hibernate工具类
 *
 * @author Huey2672
 */
public class Hibernate {
    private static final Object _locker = new Object();
    private static SessionFactory _sessionFactory;
    private static String _path = "";
    private static String _driverClass;
    private static String _driverUrl;
    private static String _dbPwd;
    private static String _dbUser;
    private static Map<String, Object> selfSettings;
    private static boolean _cache;

    /**
     * 启用hibernate缓存
     */
    public static void enableCache(){
        _cache = true;
    }
//    static boolean getCacheable() {
//        return false;
//    }
    /**
     * 配置Hibernate
     *
     * @param path hibernate配置文件路径
     */
    public static void configure(String path, Registry r, Map<String, Object> settings) {
        _path = path;
        _driverClass = r.getString("database.driver_class");
        _driverUrl = String.format(
                        "jdbc:%s://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
                        r.getString("database.driver_name"),
                        r.getString("database.host"),
                        r.getLong("database.port"),
                        r.getString("database.db"));
        _dbUser = r.getString("database.user");
        _dbPwd = r.getString("database.pwd");
        String timeZone = r.getString("database.time_zone");
        if (timeZone != null && !timeZone.equals("")) {
            try {
                _driverUrl += "&serverTimezone=" + URLEncoder.encode(timeZone, "utf-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        selfSettings = settings;
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
                _sessionFactory = createSessionFactory(_path, selfSettings);
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
     * @param path 配置文件路径
     * @return 返回工厂
     */
    private static SessionFactory createSessionFactory(String path, Map<String, Object> settings) {
        if (path == null || path.isEmpty()) {
            throw new Error("[ System][ Crash] - not set hibernate.cfg.xml path for Hibernate");
        }
        // 读取配置文件信息
        final StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().configure(path);
        builder.applySetting("hibernate.connection.driver_class", _driverClass);
        builder.applySetting("hibernate.connection.url", _driverUrl);
        builder.applySetting("hibernate.connection.username", _dbUser);
        builder.applySetting("hibernate.connection.password", _dbPwd);
        builder.applySetting("hibernate.connection.autoReconnect", true);
        builder.applySetting("hibernate.connection.autoReconnectForPools", true);
        builder.applySetting("hibernate.connection.is-connection-validation-required", true);
        builder.applySetting("hibernate.c3p0.max_statements", 0);
        builder.applySetting("hibernate.c3p0.acquire_increment", 3);
        builder.applySetting("hibernate.c3p0.testConnectionOnCheckin", true);
        builder.applySetting("hibernate.c3p0.testConnectionOnCheckout", false);
        builder.applySetting("hibernate.c3p0.idle_test_period", 3600);

        appendCustomSetting(builder, settings);
        StandardServiceRegistry registry = builder.build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static void appendCustomSetting(
            StandardServiceRegistryBuilder builder, Map<String, Object> settings) throws NullPointerException {
        if (settings != null) {
            for (String key : settings.keySet()) {
                if (!key.startsWith("hibernate.")) {
                    System.out.println("[ Hibernate][ Configure][ Warning]: unknown setting :" + key);
                } else {
                    Object v = settings.get(key);
                    if (v == null) {
                        throw new NullPointerException("[ Hibernate][ Configure]: settings " + key + " value is null");
                    }
                    builder.applySetting(key, v);
                }
            }
        }
    }

}
