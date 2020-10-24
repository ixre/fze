package net.fze.commons;


import com.moandjiezana.toml.Toml;

import java.io.File;

/**
 * 配置
 */
public class Registry {
    private final String _confPath;
    private Toml _toml;

    /**
     * 初始化配置存储
     *
     * @param confPath 配置文件目录，默认为./conf
     */
    public Registry(String confPath) {
        if (confPath.equals("")) {
            IllegalArgumentException exc = new IllegalArgumentException("no such file");
            exc.printStackTrace();
            System.exit(1);
        }
        if (confPath.substring(0, 1) == ".") {
            confPath = System.getenv("user.dir") + confPath;
        }
        this._confPath = confPath;
        this._toml = new Toml().read(new File(this._confPath));
    }

    /**
     * 获取字符串结果
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        return this._toml.getString(key);
    }

    /**
     * 获取Long
     *
     * @param key 键
     * @return 值
     */
    public Long getLong(String key) {
        return this._toml.getLong(key);
    }

    /**
     * 获取Boolean 值
     *
     * @param key 键
     * @return 值
     */
    public Boolean getBoolean(String key) {
        return this._toml.getBoolean(key);
    }
}
