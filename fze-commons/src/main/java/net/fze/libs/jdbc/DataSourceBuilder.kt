package net.fze.libs.jdbc

import net.fze.commons.Types
import java.net.URLEncoder

class DataSourceBuilder {
    private val params = ConnectionParams()
    private var poolType = Pools.Agroal
    fun create(driverClass: String, connectionUrl: String): DataSourceBuilder {
        Class.forName(driverClass) // search class
        if (!this.params.driverClass.isNullOrEmpty() || !this.params.connectionUrl.isNullOrEmpty()) {
            throw Exception("please not setting repeat!")
        }
        this.params.driverClass = driverClass
        this.params.connectionUrl = connectionUrl
        return this
    }

    fun usePool(pool: Pools): DataSourceBuilder {
        this.poolType = pool
        return this
    }

    fun credential(usr: String, pwd: String): DataSourceBuilder {
        this.params.user = usr
        this.params.pwd = pwd
        return this
    }

    fun build(): IConnectionPool {
        return when (this.poolType) {
            Pools.C3p0 -> Cp30PoolWrapper(this.params)
            Pools.Agroal-> AgroalDataSourceImpl(this.params)
            //else -> throw IllegalArgumentException("nonsupport connection pool")
        }
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
        @JvmStatic
        fun createDriverUrl(driverName: String, host: String?, port: Int, db: String?): String {
            var driverName = driverName
            var db = db
            driverName = driverName.toLowerCase()
            when (driverName) {
                "mysql", "mariadb" -> db += "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8"
            }
            return String.format("jdbc:%s://%s:%d/%s", driverName, host, port, db)
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
        @JvmStatic
        private fun createMySqlDriverUrl(driverName: String, host: String, port: Int, db: String, timezone: String): String {
            val s = String.format(
                    "jdbc:%s://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf-8",
                    driverName, host, port, db).trim { it <= ' ' }
            if (!Types.emptyOrNull(timezone)) {
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