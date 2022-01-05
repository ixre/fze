package net.fze.domain;

public final class EventBusTypes {
    public static <T> String getName(T t) {
        return t.getClass().getName();
    }
}
