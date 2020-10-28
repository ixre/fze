package net.fze.common.std;

public interface Creator<T> {
    Creator<?> CLASS = it -> {
        try {
            return it.getDeclaredConstructor().newInstance();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    };

    T create(Class<?> c);
}