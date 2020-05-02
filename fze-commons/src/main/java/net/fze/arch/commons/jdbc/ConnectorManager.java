package net.fze.arch.commons.jdbc;

import net.fze.arch.commons.std.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 连接器管理器
 */
public class ConnectorManager {
    public List<String> keys = new ArrayList<>();
    public Map<String, JdbcConnector> data = new LinkedHashMap<>();
    private String defaultKey = "";

    public void put(String key, JdbcConnector c) {
        this.data.put(key, c);
        this.keys.add(key);
    }

    public JdbcConnector get(String key) {
        return this.data.get(key);
    }

    /**
     * 获取第一个JDBC连接
     */
    public JdbcConnector first() {
        if (this.keys.size() > 0) {
            return this.data.get(this.keys.get(0));
        }
        return null;
    }

    /**
     * 获取默认的JDBC连接
     */
    public JdbcConnector getDefault() {
        if (Types.emptyOrNull(this.defaultKey)) {
            return this.first();
        }
        if (!this.keys.contains(this.defaultKey)) {
            throw new Error("不包含默认的JDBC连接:" + this.defaultKey);
        }
        return this.data.get(this.defaultKey);
    }

    /**
     * 设置默认的JDBC连接
     *
     * @param key 键
     */
    public void setDefault(String key) {
        this.defaultKey = key;
    }
}