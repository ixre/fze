package net.fze.arch.commons.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface IConnector {
    /**
     * 获取连接URL
     */
    String driverUrl();

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource dataSource();

    /**
     * 获取连接
     */
    Connection open() throws SQLException;

    /**
     * 设置最大连接数据
     */
    void setMaxOpenConn(int n);

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    void setMaxIdleConn(int n);

    /**
     * 设置连接最长存活时间
     *
     * @param second 秒
     */
    void setConnMaxLifetime(int second);

    /**
     * 设置在检出数据时是否测试连接
     * @param b 是/否
     */
    void setTestConnectionOnCheckout(boolean b);

    /**
     * 设置在检入数据时是否测试连接
     * @param b 是/否
     */
    void setTestConnectionOnCheckin(boolean b);

    /**
     * 设置空闲时间保持连接的间隔时间
     * @param seconds 秒,默认3600秒
     */
    void setIdleConnectionTestPeriod(int seconds);
}
