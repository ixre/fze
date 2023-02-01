package net.fze.jdk.jdk8;


import java.util.ArrayList;
import java.util.List;

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

}