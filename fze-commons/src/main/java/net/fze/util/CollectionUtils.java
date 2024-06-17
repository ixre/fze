package net.fze.util;

import net.fze.jdk.IndexSupplier;
import net.fze.jdk.jdk8.Lists;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 主要为了解决JDK高版本不支持Map.of方法
 * @author jarrysix
 */
public interface CollectionUtils {
    /**
     * exclude keys
     */
    @SafeVarargs
    static <K, V> Map<K, V> excludes(Map<K, V> s, K... keys) {
        for (K k : keys) {
            s.remove(k);
        }
        return s;
    }

    /**
     * pick keys
     */
    @SafeVarargs
    static <K, V> Map<K, V> picks(Map<K, V> s, K... keys) {
        List<K> ks = Lists.of(keys);
        s.keySet().removeIf(k -> !ks.contains(k));
        return s;
    }

    /**
     * 去重
     */
    static <E> Collection<E> removeRepeatElement(Iterable<E> list) {
        LinkedHashSet<E> set = new LinkedHashSet<E>();
        list.forEach(set::add);
        return set;
    }

    /**
     * for each with index
     *
     * @param list 列表
     * @param supplier 迭代函数
     * @param <T> 类型
     * @return 迭代器
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
    @SuppressWarnings("unchecked")
    static <T, K> K[] asArray(List<T> list, Types.TProp<T, K> p) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int i = 0;
        K tmp;
        K[] arr = null;
        for (T e : list) {
            tmp = p.get(e);
            if (arr == null) {
                Class<?> c = tmp.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null) {
                    c = cc;
                }
                arr = (K[]) Array.newInstance(c, list.size());
            }
            arr[i++] = p.get(e);
        }
        return arr;
    }

    /**
     * 将集合转成数组
     */
    @SuppressWarnings("unchecked")
    static <T> T[] toArray(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int i = 0;
        T[] arr = null;
        for (T e : list) {
            if (arr == null) {
                Class<?> c = e.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null) {
                    c = cc;
                }
                arr = (T[]) Array.newInstance(c, list.size());
            }
            arr[i++] = e;
        }
        return arr;
    }


    /**
     * 判断集合是否存在交集数据
     *
     * @param arr    集合1
     * @param arrTwo 结合2
     * @param <T>    类型
     * @return 交集列表
     */
    static <T> List<T> findSame(Collection<T> arr, Collection<T> arrTwo) {
        Hashtable<T, Integer> ht = new Hashtable<>();
        for (T t : arr) {
            ht.put(t, 1);
        }
        List<T> list = new ArrayList<>();
        for (T t : arrTwo) {
            if (ht.containsKey(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
