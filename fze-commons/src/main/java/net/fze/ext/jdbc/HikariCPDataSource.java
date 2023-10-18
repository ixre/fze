package net.fze.ext.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource implements IConnectionPool {
    private final HikariDataSource ds;
    private final String driverUrl;
    private final String usr;
    private final String pwd;

    /**
     * 创建连接器
     *
     * @param p 连接参数
     */
    HikariCPDataSource(ConnectionParams p) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(p.getConnectionUrl());
        config.setUsername(p.getUser());
        config.setPassword(p.getPwd());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        ds.setDriverClassName(p.getDriverClass());
        ds.setJdbcUrl( p.getConnectionUrl());
        ds.setUsername(p.getUser());
        ds.setPassword( p.getPwd());
        ds.setIdleTimeout(3600);
        // 避免超过8小时连接断开
//        ds.isTestConnectionOnCheckout = false
//        ds.isTestConnectionOnCheckin = true
//        ds.idleConnectionTestPeriod = 3600
//        ds.preferredTestQuery = "SELECT 1;"
        // properties = Properties(ds)
        this.driverUrl = p.getConnectionUrl();
        this.usr = p.getUser();
        this.pwd = p.getPwd();
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
     */
    public String user() {
        return this.usr;
    }

    /**
     * 用户密码
     *
     */

    public String pwd() {
        return this.pwd;
    }

    /**
     * 获取连接
     */
    @Override
    public Connection open() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 获取连接属性
     *
     */
    public Properties properties() {
        throw new IllegalStateException("not implement");
    }

    /**
     * 获取JDB连接
     *
     * @return 返回连接，如果连接失败，则返回null
     */
    @Override
    public JdbcConnection acquire() {
        try {
            Connection conn = ds.getConnection();
            return new JdbcConnection(conn);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置最大连接数据
     */
    @Override
    public void setMaxOpenConn(int n) {
        ds.setMaximumPoolSize(n);
    }

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    @Override
    public void setMaxIdleConn(int n) {
        ds.setMinimumIdle(n);
    }
    @Override
    public void setConnMaxLifetime(int second) {
        try {
            ds.setLoginTimeout(second);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置在检出数据时是否测试连接
     *
     * @param b 是/否
     */
    @Override
    public void setTestConnectionOnCheckout(boolean b) {

    }

    /**
     * 设置在检入数据时是否测试连接
     *
     * @param b 是/否
     */
    @Override
    public void setTestConnectionOnCheckin(boolean b) {

    }

    /**
     * 设置空闲时间保持连接的间隔时间
     *
     * @param seconds 秒,默认3600秒
     */
    @Override
    public void setIdleConnectionTestPeriod(int seconds) {
        //this.ds.setIdleTimeout(seconds);
    }


}
