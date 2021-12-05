package net.fze.jdk;

import java.util.HashMap;
import java.util.Map;

/**
 * 主要为了解决JDK高版本不支持Map.of方法
 */
public interface Maps {
    /**
     * Returns an unmodifiable map containing zero mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return an empty {@code Map}
     */
    static <K, V> Map<K, V> of() {
        return new HashMap<K, V>();
    }

    /**
     * Returns an unmodifiable map containing a single mapping.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the mapping's key
     * @param v1  the mapping's value
     * @return a {@code Map} containing the specified mapping
     * @throws NullPointerException if the key or the value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1) {
        Map<K, V> m = of();
        m.put(k1, v1);
        return m;
    }

    /**
     * Returns an unmodifiable map containing two mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if the keys are duplicates
     * @throws NullPointerException     if any key or value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> m = of();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    /**
     * Returns an unmodifiable map containing three mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> m = of();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return m;
    }

    /**
     * Returns an unmodifiable map containing four mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> m = of();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        return m;
    }

    /**
     * Returns an unmodifiable map containing five mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> m = of();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        m.put(k5, v5);
        return m;
    }

    /**
     * Returns an unmodifiable map containing five mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's valur
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     */
    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> m = of();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        m.put(k5, v5);
        m.put(k6, v6);
        return m;
    }
}
