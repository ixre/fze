package net.fze.jdk;

import java.util.ArrayList;
import java.util.List;

public interface Lists {
    /**
     * Returns an unmodifiable list containing zero elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @return an empty {@code List}
     *
     */
    @SuppressWarnings("unchecked")
    static <E> List<E> of(){
        return new ArrayList<>();
    }

    /**
     * Returns an unmodifiable list containing one element.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the single element
     * @return a {@code List} containing the specified element
     * @throws NullPointerException if the element is {@code null}
     *
     */
    static <E> List<E> of(E e1){
        List<E> l = of();
        l.add(e1);
        return l;
    }

    /**
     * Returns an unmodifiable list containing two elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the first element
     * @param e2 the second element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     *
     */
    static <E> List<E> of(E e1, E e2) {
        List<E> l = of();
        l.add(e1);
        l.add(e2);
        return l;
    }

    /**
     * Returns an unmodifiable list containing three elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the first element
     * @param e2 the second element
     * @param e3 the third element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     *
     */
    static <E> List<E> of(E e1, E e2, E e3) {
        List<E> l = of();
        l.add(e1);
        l.add(e2);
        l.add(e3);
        return l;
    }

    /**
     * Returns an unmodifiable list containing four elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the first element
     * @param e2 the second element
     * @param e3 the third element
     * @param e4 the fourth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     *
     */
    static <E> List<E> of(E e1, E e2, E e3, E e4) {

        List<E> l = of();
        l.add(e1);
        l.add(e2);
        l.add(e3);
        l.add(e4);
        return l;
    }

    /**
     * Returns an unmodifiable list containing five elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the first element
     * @param e2 the second element
     * @param e3 the third element
     * @param e4 the fourth element
     * @param e5 the fifth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     *
     */
    static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {

        List<E> l = of();
        l.add(e1);
        l.add(e2);
        l.add(e3);
        l.add(e4);
        l.add(e5);
        return l;
    }

    /**
     * Returns an unmodifiable list containing six elements.
     *
     * See <a href="#unmodifiable">Unmodifiable Lists</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1 the first element
     * @param e2 the second element
     * @param e3 the third element
     * @param e4 the fourth element
     * @param e5 the fifth element
     * @param e6 the sixth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     *
     */
    static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        List<E> l = of();
        l.add(e1);
        l.add(e2);
        l.add(e3);
        l.add(e4);
        l.add(e5);
        l.add(e6);
        return l;
    }
}
