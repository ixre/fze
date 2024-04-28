package net.fze.jdk.jdk8;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}