package net.fze.jdk.jdk8;


import net.fze.util.Strings;
import net.fze.util.TypeConv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jarrysix
 */
public interface Lists {
    /**
     * Returns an unmodifiable list containing zero elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @return an empty {@code List}
     */
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
    @SafeVarargs
    static <E> List<E> of(E... args) {
        List<E> l = of();
        Collections.addAll(l, args);
        return l;
    }

    /**
     * Returns an unmodifiable list containing the elements of the specified array.
     * @param s the array whose elements are to be placed into the list
     * @param delimiter 分隔符
     * @return a {@code List} containing the elements of the specified array
     */
    static List<String> split(String s,String delimiter){
        if(Strings.isNullOrEmpty(s)){
            return of();
        }
        return of(s.split(delimiter));
    }

    /**
     * Returns an unmodifiable list containing the elements of the specified array.
     * @param s the array whose elements are to be placed into the list
     * @param delimiter 分隔符
     * @return a {@code List} containing the elements of the specified array
     */
    static List<Integer> splitInt(String s,String delimiter){
        return split(s,delimiter).stream().map(TypeConv::toInt).collect(Collectors.toList());
    }
}