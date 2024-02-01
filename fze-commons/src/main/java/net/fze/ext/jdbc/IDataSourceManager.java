package net.fze.ext.jdbc;

/**
 * 数据源管理器
 */
public interface IDataSourceManager {
    /**
     * 存储连接池
     */
    void put(String key, IConnectionPool c);

    /**
     * 获取连接池
     */
    IConnectionPool get(String key);

    /**
     * 获取默认的JDBC连接
     */
     IConnectionPool getDefault();
}
