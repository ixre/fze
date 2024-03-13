package net.fze.ext.storage;

/**
 * 存储提供者
 *  移植自 gof: <a href="https://github.com/jsix/gof/tree/master/storage">github.com/jsix/gof</a>
 * @author jarrysix
 */
// Storage
public interface IStorageProvider {
    // Return storage driver name
    String driver();

    // return storage source
    Object source();

    // Check key is existing or not
    boolean exists(String key);

    // Set Value
    void set(String key, Object v);

    // Auto Delete Set
    void setExpire(String key, Object v, long seconds);

    // Get Value
    <T> T get(String key, Class<T> c);

    // Get raw value
    Object getRaw(String key);

    Boolean getBool(String key);

    int getInt(String key);

    long getInt64(String key);

    String getString(String key);

    float getFloat(String key);

    byte[] getBytes(String key);

    // Delete Storage
    void remove(String pattern);
}
