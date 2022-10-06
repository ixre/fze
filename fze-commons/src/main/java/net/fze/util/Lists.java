package net.fze.util;

import net.fze.jdk.IndexSupplier;

import java.lang.reflect.Array;
import java.util.*;

public interface Lists {
    /**
     * Returns an unmodifiable list containing zero elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @return an empty {@code List}
     */
    @SuppressWarnings("unchecked")
    static <E> List<E> of() {
        return new ArrayList<>();
    }

    /**
     * Returns an unmodifiable list containing one element.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @return a {@code List} containing the specified element
     * @throws NullPointerException if the element is {@code null}
     */
    static <E> List<E> of(E... args) {
        List<E> l = of();
        for (E e : args) {
            l.add(e);
        }
        return l;
    }

    /** 去重 */
    static <E> Collection<E> removeRepeatElement(Iterable<E> list) {
        LinkedHashSet<E> set = new LinkedHashSet<E>();
        list.forEach((it) -> set.add(it));
        return set;
    }

    /**
     * 按顺序排列
     */
    static <T> List<T> sort(List<T> list, Comparator<T> c) {
        list.sort(c);
        return list;
    }

    /**
     * 将列表顺序颠倒
     */
    static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 按倒序排列
     */
    static <T> List<T> sortByDescending(List<T> list, Comparator<T> c) {
        List<T> dst = sort(list, c);
        return reverse(dst);
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

    static <T> void each(Iterable<T> e, Types.TCond<T> c) {
        for (T t : e)
            if (!c.test(t))
                break;
    }

    static <T> void each(Iterable<T> e, Types.TFunc<T> f) {
        for (T t : e)
            f.call(t);
    }

    static <T> void eachArray(T[] e, Types.TCond<T> c) {
        for (T t : e)
            if (!c.test(t))
                break;
    }

    static <T> void eachArray(T[] e, Types.TFunc<T> f) {
        for (T t : e)
            f.call(t);
    }

}
