package net.fze.jdk;

public interface IndexSupplier<T> {
    void apply(T t, int index);
}
