package net.fze.arch.commons.std.storage;

// 移植自< gof >: https://github.com/jsix/gof/tree/master/storage
// Storage
public interface Storage {
    // Return storage driver name
    String driver();

    // return storage source
    Object source();

    // Check key is exists or not
    boolean exists(String key);

    // Set Value
    Error set(String key, Object v);

    // Auto Delete Set
    Error setExpire(String key, Object v, int seconds);

    // Get Value
    <T> T get(String key, Class<T> c);

    //Get raw value
    Object getRaw(String key);

    Boolean getBool(String key);

    int getInt(String key);

    long getInt64(String key);

    String getString(String key);

    float getFloat(String key);

    byte[] getBytes(String key);

    // Delete Storage
    void del(String pattern);
}
