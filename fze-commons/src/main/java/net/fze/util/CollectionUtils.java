package net.fze.util;

import net.fze.jdk.IndexSupplier;
import net.fze.jdk.jdk8.Lists;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 主要为了解决JDK高版本不支持Map.of方法
 */
public interface CollectionUtils {
    /**
     * exclude keys
     */
    static <K, V> Map<K, V> excludes(Map<K, V> s, K... keys) {
        for (K k : keys) {
            if (s.containsKey(k))
                s.remove(k);
        }
        return s;
    }

    /**
     * pick keys
     */
    static <K, V> Map<K, V> picks(Map<K, V> s, K... keys) {
        List<K> ks = Lists.of(keys);
        Iterator<K> iterator = s.keySet().iterator();
        while (iterator.hasNext()) {
            K k = iterator.next();
            if (!ks.contains(k))
                iterator.remove();
        }
        return s;
    }

    /** 去重 */
    static <E> Collection<E> removeRepeatElement(Iterable<E> list) {
        LinkedHashSet<E> set = new LinkedHashSet<E>();
        list.forEach((it) -> set.add(it));
        return set;
    }

    /**
     * for each with index
     *
     * @param list
     * @param supplier
     * @param <T>
     * @return
     */
    static <T> Iterable<T> forEachWithIndex(Iterable<T> list, IndexSupplier<T> supplier) {
        Iterator<T> iterator = list.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            supplier.apply(iterator.next(), i++);
        }
        return list;
    }

    /**
     * 作为数组返回
     *
     * @param list 列表
     * @param p    表达式
     * @return 数组
     */
    static <T, K> K[] asArray(List<T> list, Types.TProp<T, K> p) {
        if (list == null || list.size() == 0)
            return null;
        int i = 0;
        K tmp;
        K[] arr = null;
        for (T e : list) {
            tmp = p.get(e);
            if (arr == null) {
                Class<?> c = tmp.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null)
                    c = cc;
                arr = (K[]) Array.newInstance(c, list.size());
            }
            arr[i++] = p.get(e);
        }
        return arr;
    }

    /**
     * 将集合转成数组
     */
    static <T> T[] toArray(Collection<T> list) {
        if (list == null || list.size() == 0)
            return null;
        int i = 0;
        T[] arr = null;
        for (T e : list) {
            if (arr == null) {
                Class<?> c = e.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null)
                    c = cc;
                arr = (T[]) Array.newInstance(c, list.size());
            }
            arr[i++] = e;
        }
        return arr;
    }
}
