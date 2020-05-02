package net.fze.arch.commons.jdbc;

import net.fze.arch.commons.std.Types;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.net.URLEncoder;
import java.sql.SQLException;

public class JdbcConnector implements IConnector {
    private final ComboPooledDataSource ds;
    private final Properties properties;
    private final String driverUrl;
    private final String usr;
    private final String pwd;

    /**
     * 创建连接器
     *
     * @param driverClass   驱动类
     * @param connectionUrl 连接字符串
     * @param usr           用户
     * @param pwd           密码
     */
    public JdbcConnector(String driverClass, String connectionUrl, String usr, String pwd)
            throws ClassNotFoundException, PropertyVetoException {
        Class.forName(driverClass);
        this.ds = new ComboPooledDataSource();
        ds.setDriverClass(driverClass);
        ds.setJdbcUrl(connectionUrl);
        ds.setUser(usr);
        ds.setPassword(pwd);
        ds.setAcquireIncrement(3);
        // 避免超过8小时连接断开
        ds.setTestConnectionOnCheckout(false);
        ds.setTestConnectionOnCheckin(true);
        ds.setIdleConnectionTestPeriod(3600);
        ds.setPreferredTestQuery("SELECT 1;");
        this.properties = new Properties(this.ds);
        this.driverUrl = connectionUrl;
        this.usr = usr;
        this.pwd = pwd;
    }

    /**
     * 创建MySql连接URL
     *
     * @param driverName 驱动名称
     * @param host       主机
     * @param port       端口
     * @param db         数据库
     */
    public static String createDriverUrl(String driverName, String host, int port, String db) {
        driverName = driverName.toLowerCase();
        switch (driverName) {
            case "mysql":
            case "mariadb":
                db += "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
                break;
        }
        return String.format("jdbc:%s://%s:%d/%s", driverName, host, port, db);
    }

    @Deprecated
    public static String createDriverUrl(String driverName, String host, int port, String db, String timezone) {
        return createMySqlDriverUrl(driverName, host, port, db, timezone);
    }

    /**
     * 创建MySql连接URL
     *
     * @param driverName 驱动名称
     * @param host       主机
     * @param port       端口
     * @param db         数据库
     * @param timezone   时区,可为空
     */
    private static String createMySqlDriverUrl(String driverName, String host, int port, String db, String timezone) {
        String s = String.format(
                "jdbc:%s://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf-8",
                driverName, host, port, db).trim();
        if (!Types.emptyOrNull(timezone)) {
            try {
                return s + "&serverTimezone=" + URLEncoder.encode(timezone, "utf-8");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 获取连接URL
     */
    @Override
    public String driverUrl() {
        return this.driverUrl;
    }

    @Override
    public DataSource dataSource() {
        return this.ds;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String user() {
        return this.usr;
    }

    /**
     * 用户密码
     *
     * @return
     */
    public String pwd() {
        return this.pwd;
    }

    /**
     * 获取连接
     *
     * @return
     */
    @Override
    public java.sql.Connection open() throws SQLException {
        return this.ds.getConnection();
    }

    /**
     * 获取连接属性
     *
     * @return
     */
    public Properties properties() {
        return this.properties;
    }

    /**
     * 获取JDB连接
     *
     * @return 返回连接，如果连接失败，则返回null
     */
    public JdbcConnection acquire() {
        try {
            java.sql.Connection conn = this.ds.getConnection();
            return new JdbcConnection(conn);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return new JdbcConnection(null);
    }

    /**
     * 设置最大连接数据
     */
    @Override
    public void setMaxOpenConn(int n) {
        this.ds.setMaxPoolSize(n);
    }

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    @Override
    public void setMaxIdleConn(int n) {
        this.ds.setMaxIdleTimeExcessConnections(n);
    }

    @Override
    public void setConnMaxLifetime(int second) {
        try {
            this.ds.setLoginTimeout(second);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setTestConnectionOnCheckout(boolean b) {
        this.ds.setTestConnectionOnCheckout(b);
    }

    @Override
    public void setTestConnectionOnCheckin(boolean b) {
        this.ds.setTestConnectionOnCheckin(b);
    }

    @Override
    public void setIdleConnectionTestPeriod(int seconds) {
        this.ds.setIdleConnectionTestPeriod(seconds);
    }
}
