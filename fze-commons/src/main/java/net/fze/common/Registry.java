package net.fze.common;

import com.moandjiezana.toml.Toml;
import net.fze.util.Strings;

import java.io.File;

/**
 * 配置
 */
public class Registry implements IRegistry {
    private final Toml _toml;

    /**
     * 初始化配置存储
     *
     * @param confPath 配置文件目录，默认为./conf
     */
    public Registry(String confPath) {
        if (confPath.isEmpty()) {
            IllegalArgumentException exc = new IllegalArgumentException("no such file");
            exc.printStackTrace();
            System.exit(1);
        }
        if (confPath.charAt(0) == '.') {
            String userDir = System.getenv("user.dir");
            if (Strings.isNullOrEmpty(userDir)) {
                userDir = System.getProperty("user.dir");
            }
            if(Strings.isNullOrEmpty(userDir)) {
                confPath = userDir + confPath.substring(1);
            }
        }
        String _confPath = confPath;
        this._toml = new Toml().read(new File(_confPath));
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
        return this._toml.getLong(key, 0L);
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

    /**
     * 获取int32
     *
     * @param key 键
     */
    @Override
    public Integer getInteger(String key) {
        return this._toml.getLong(key, 0L).intValue();
    }
}
