package net.fze.lib.jdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

class HikariCPWrapper(p: ConnectionParams) : IConnectionPool {
    private lateinit var ds: HikariDataSource
    private val driverUrl: String
    private val usr: String
    private val pwd: String

    /**
     * 创建连接器
     *
     * @param p   连接参数
     */
    init {
        val config = HikariConfig()
        config.setJdbcUrl(p.connectionUrl);
        config.setUsername(p.user);
        config.setPassword(p.pwd);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        ds = HikariDataSource(config)
//        ds.driverClass = p.driverClass
//        ds.jdbcUrl = p.connectionUrl
//        ds.user = p.user
//        ds.password = p.pwd
//        ds.acquireIncrement = 3
//        // 避免超过8小时连接断开
//        ds.isTestConnectionOnCheckout = false
//        ds.isTestConnectionOnCheckin = true
//        ds.idleConnectionTestPeriod = 3600
//        ds.preferredTestQuery = "SELECT 1;"
      // properties = Properties(ds)
        driverUrl = p.connectionUrl
        usr = p.user
        pwd = p.pwd
    }

    /**
     * 获取连接URL
     */
    override fun driverUrl(): String {
        return driverUrl
    }

    override fun dataSource(): DataSource {
        return ds
    }

    /**
     * 获取用户名
     *
     * @return
     */
    fun user(): String {
        return usr
    }

    /**
     * 用户密码
     *
     * @return
     */
    fun pwd(): String {
        return pwd
    }

    /**
     * 获取连接
     *
     * @return
     */
    @Throws(SQLException::class)
    override fun open(): Connection {
        return ds.connection
    }

    /**
     * 获取连接属性
     *
     * @return
     */
    fun properties(): Properties {
        throw IllegalStateException("not implement")
    }

    /**
     * 获取JDB连接
     *
     * @return 返回连接，如果连接失败，则返回null
     */
    override fun acquire(): JdbcConnection {
        try {
            val conn = ds.connection
            return JdbcConnection(conn)
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
        return JdbcConnection(null)
    }

    /**
     * 设置最大连接数据
     */
    override fun setMaxOpenConn(n: Int) {
        ds.maximumPoolSize = n
    }

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    override fun setMaxIdleConn(n: Int) {
        ds.minimumIdle = n
    }

    override fun setConnMaxLifetime(second: Int) {
        try {
            ds.loginTimeout = second
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    override fun setTestConnectionOnCheckout(b: Boolean) {
        throw IllegalStateException("not implement")
    }

    override fun setTestConnectionOnCheckin(b: Boolean) {
        throw IllegalStateException("not implement")
    }

    override fun setIdleConnectionTestPeriod(seconds: Int) {
        throw IllegalStateException("not implement")
    }

}