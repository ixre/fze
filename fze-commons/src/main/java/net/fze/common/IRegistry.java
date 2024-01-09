package net.fze.common;

public interface IRegistry {
    /**
     * 获取字符串结果
     *
     * @param key 键
     * @return 值
     */
     String getString(String key);

    /**
     * 获取Long
     *
     * @param key 键
     * @return 值
     */
     Long getLong(String key);

    /**
     * 获取Boolean 值
     *
     * @param key 键
     * @return 值
     */
     Boolean getBoolean(String key);


    /**
     * 获取Boolean 值
     *
     * @param key 键
     * @return 值
     */
     Integer getInteger(String key);
}
