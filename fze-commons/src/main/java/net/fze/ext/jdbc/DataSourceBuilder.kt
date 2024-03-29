package net.fze.ext.jdbc

import net.fze.util.Strings
import java.net.URLEncoder

class DataSourceBuilder// search class
    (driverClass: String) {
    private val params = ConnectionParams()
    private var poolType = Pools.HikariCP;

    init {
        Class.forName(driverClass)
        this.params.driverClass = driverClass
    }

    fun setJdbcUrl(connUrl: String): DataSourceBuilder {
        if (this.params.driverClass.isEmpty()) {
            throw Exception("please setting driver class first !")
        }
        this.params.connectionUrl = connUrl
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
        if(!Strings.isNullOrEmpty(this.params.driverClass)){
            Class.forName(this.params.driverClass);
        }
        return when (this.poolType) {
            Pools.HikariCP -> HikariCPDataSource(this.params)
            Pools.C3p0 -> Cp30PoolWrapper(this.params)
            Pools.Agroal -> AgroalDataSourceImpl(this.params)
            //else -> throw IllegalArgumentException("nonsupport connection pool")
        }
    }

    companion object {
        @JvmStatic
        fun create(driverClass: String): DataSourceBuilder {
            return DataSourceBuilder(driverClass)
        }

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
            var retDriverName = driverName
            var retDb = db
            retDriverName = retDriverName.lowercase()
            when (retDriverName) {
                "mysql", "mariadb" -> retDb += "?allowMultiQueries=true&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf-8"
            }
            return String.format("jdbc:%s://%s:%d/%s", retDriverName, host, port, retDb)
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
        private fun createMySqlDriverUrl(
            driverName: String,
            host: String,
            port: Int,
            db: String,
            timezone: String
        ): String {
            val s = String.format(
                "jdbc:%s://%s:%d/%s?allowMultiQueries=true&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf-8",
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