package net.fze.lib.jdbc

import com.mchange.v2.c3p0.ComboPooledDataSource
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

class Cp30PoolWrapper(p: ConnectionParams) : IConnectionPool {
    private val ds: ComboPooledDataSource = ComboPooledDataSource()
    private val properties: Properties
    private val driverUrl: String
    private val usr: String
    private val pwd: String

    /**
     * 创建连接器
     *
     * @param p   连接参数
     */
    init {
        ds.driverClass = p.driverClass
        ds.jdbcUrl = p.connectionUrl
        ds.user = p.user
        ds.password = p.pwd
        ds.acquireIncrement = 3
        // 避免超过8小时连接断开
        ds.isTestConnectionOnCheckout = false
        ds.isTestConnectionOnCheckin = true
        ds.idleConnectionTestPeriod = 3600
        ds.preferredTestQuery = "SELECT 1;"
        properties = Properties(ds)
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
        return properties
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
        ds.maxPoolSize = n
    }

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    override fun setMaxIdleConn(n: Int) {
        ds.maxIdleTimeExcessConnections = n
    }

    override fun setConnMaxLifetime(second: Int) {
        try {
            ds.loginTimeout = second
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    override fun setTestConnectionOnCheckout(b: Boolean) {
        ds.isTestConnectionOnCheckout = b
    }

    override fun setTestConnectionOnCheckin(b: Boolean) {
        ds.isTestConnectionOnCheckin = b
    }

    override fun setIdleConnectionTestPeriod(seconds: Int) {
        ds.idleConnectionTestPeriod = seconds
    }

}