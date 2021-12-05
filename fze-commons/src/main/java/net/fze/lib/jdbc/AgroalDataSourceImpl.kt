package net.fze.lib.jdbc

import io.agroal.api.AgroalDataSource
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier
import io.agroal.api.security.NamePrincipal
import io.agroal.api.security.SimplePassword
import net.fze.util.Strings
import java.net.URLEncoder
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

class AgroalDataSourceImpl(p: ConnectionParams) : IConnectionPool {
    private var ds: AgroalDataSource
    private val properties: Properties? = null
    private val driverUrl: String
    private val usr: String
    private val pwd: String

    /**
     * 创建连接器
     *
     * @param p   连接参数
     */
    init {

        val supplier = AgroalDataSourceConfigurationSupplier()

        supplier.connectionPoolConfiguration()
            .connectionFactoryConfiguration()
            .connectionProviderClassName(p.driverClass)
            .jdbcUrl(p.connectionUrl)
            .principal(NamePrincipal(p.user))
            .credential(SimplePassword(p.pwd))
        //.recoveryPrincipal( NamePrincipal("testuser"))
        //.recoveryCredential( SimplePassword("testpass"))
        //.maxSize(10)


        //   val configure =
        this.ds = AgroalDataSource.from(supplier)
        //ds.acquireIncrement = 3
        // 避免超过8小时连接断开
//        ds.isTestConnectionOnCheckout = false
//        ds.isTestConnectionOnCheckin = true
//        ds.idleConnectionTestPeriod = 3600
//        ds.preferredTestQuery = "SELECT 1;"
        //properties = Properties(ds)
        driverUrl = p.connectionUrl
        usr = p.user
        pwd = p.pwd
        this.ds = AgroalDataSource.from(supplier);
    }

    /**
     * 获取连接URL
     */
    override fun driverUrl(): String {
        return driverUrl
    }

    override fun dataSource(): DataSource {
        throw NotImplementedError("not implement")

        //return ds
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
        return this.ds!!.connection
    }

    /**
     * 获取连接属性
     *
     * @return
     */
    fun properties(): Properties {
        throw NotImplementedError("not implement")
        // return properties
    }

    /**
     * 获取JDB连接
     *
     * @return 返回连接，如果连接失败，则返回null
     */
    override fun acquire(): JdbcConnection {
        try {
            val conn = this.ds!!.connection
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
        throw NotImplementedError("not implement")
        // ds.maxPoolSize = n
    }

    /**
     * 最值最大空闲连接
     *
     * @param n 连接数
     */
    override fun setMaxIdleConn(n: Int) {
        throw NotImplementedError("not implement")
        // ds.maxIdleTimeExcessConnections = n
    }

    override fun setConnMaxLifetime(second: Int) {
        try {
            ds.loginTimeout = second
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    override fun setTestConnectionOnCheckout(b: Boolean) {
        throw NotImplementedError("not implement")

        // ds.isTestConnectionOnCheckout = b
    }

    override fun setTestConnectionOnCheckin(b: Boolean) {
        throw NotImplementedError("not implement")
        //ds.isTestConnectionOnCheckin = b
    }

    override fun setIdleConnectionTestPeriod(seconds: Int) {
        throw NotImplementedError("not implement")
        //ds.idleConnectionTestPeriod = seconds
    }

    companion object {
        /**
         * 创建MySql连接URL
         *
         * @param driverName 驱动名称
         * @param host       主机
         * @param port       端口
         * @param db         数据库
         */
        fun createDriverUrl(driverName: String, host: String?, port: Int, db: String?): String {
            var driverName = driverName
            var db = db
            driverName = driverName.toLowerCase()
            when (driverName) {
                "mysql", "mariadb" -> db += "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8"
            }
            return String.format("jdbc:%s://%s:%d/%s", driverName, host, port, db)
        }

        @Deprecated("")
        fun createDriverUrl(driverName: String, host: String, port: Int, db: String, timezone: String): String {
            return createMySqlDriverUrl(driverName, host, port, db, timezone)
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
        private fun createMySqlDriverUrl(
            driverName: String,
            host: String,
            port: Int,
            db: String,
            timezone: String
        ): String {
            val s = String.format(
                "jdbc:%s://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf-8",
                driverName, host, port, db
            ).trim { it <= ' ' }
            if (!Strings.isNullOrEmpty(timezone)) {
                try {
                    return s + "&serverTimezone=" + URLEncoder.encode(timezone, "utf-8")
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            return s
        }
    }


}