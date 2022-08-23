package net.fze.util;

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
        for(E e : args){
            l.add(e);
        }
        return l;
    }

    /** 去重 */
    static <E> Collection<E> removeRepeatElement(Iterable<E> list){
        LinkedHashSet<E> set = new LinkedHashSet<E>();
        list.forEach((it)->set.add(it));
        return set;
    }


    /**
     * 按顺序排列
     */
    static <T> List<T> sort(List<T> list,Comparator<T> c) {
         list.sort(c);
         return list;
    }

    /**
     * 将列表顺序颠倒
     */
    static <T> List<T>  reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 按倒序排列
     */
    static <T>  List<T> sortByDescending(List<T> list, Comparator<T> c){
        List<T> dst = sort(list, c);
        return reverse(dst);
    }
}
