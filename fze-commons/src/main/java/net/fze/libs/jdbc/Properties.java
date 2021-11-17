package net.fze.libs.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.SQLException;

public class Properties {

    public final ComboPooledDataSource ds;

    Properties(ComboPooledDataSource ds) {
        this.ds = ds;
    }

    /**
     * 获取连接池
     *
     * @return 返回连接池
     */
    //public ComboPooledDataSource pool() {
    //    return this.ds;
    //}

    /**
     * 设置间隔尝试连接
     *
     * @param second 秒数
     */
    public void setIdleConnectionTestPeriod(int second) {
        this.ds.setIdleConnectionTestPeriod(second);
    }

    /**
     * 设置登陆超时时间
     *
     * @param s 秒数
     * @throws SQLException
     */
    public void setLoginTimeout(int s) throws SQLException {
        this.ds.setLoginTimeout(s);
    }

    /**
     * 设置最大空闲时间，超过时间则自动丢弃连接
     *
     * @param s 秒数
     */
    public void setMaxIdleTime(int s) {
        this.ds.setMaxIdleTime(s);
    }

    /**
     * 当连接耗尽时，一次获取的连接数量，默认为：3
     *
     * @param n 连接数
     */
    public void setAcquireIncrement(int n) {
        this.ds.setAcquireIncrement(n);
    }

    /**
     * 设置测试连接的SQL语句
     *
     * @param query 查询语句
     */
    public void setPreferredTestQuery(String query) {
        this.ds.setPreferredTestQuery(query);
    }

    public void setMaxIdleTimeExcessConnections(int n) {
        this.ds.setMaxIdleTimeExcessConnections(n);
    }

    /**
     * 设置最长连接时间
     *
     * @param s 秒数
     */
    public void setMaxConnectionAge(int s) {
        this.ds.setMaxConnectionAge(s);
    }

    /**
     * 设置连接池最大连接数量
     *
     * @param n 数量
     */
    public void setMaxPoolSize(int n) {
        this.ds.setMaxPoolSize(n);
    }

    /**
     * 设置为true，所有的连接都将检测其有效性，会影响性能，所以将其设置为false
     *
     * @param b
     */
    public void setTestConnectionOnCheckOut(boolean b) {
        this.ds.setTestConnectionOnCheckout(b);
    }

    /**
     * 异步检测连接的有效性
     *
     * @param b
     */
    public void setTestConnectionOnCheckIn(boolean b) {
        this.ds.setTestConnectionOnCheckin(b);
    }

    /**
     * 设置连接池初始化时连接数量，默认为：3
     *
     * @param n 数量
     */
    public void setInitialPoolSize(int n) {
        this.ds.setInitialPoolSize(n);
    }
}
