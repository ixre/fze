package net.fze.domain;

final class EventBusTypes {
    public static <T> String getName(T t) {
        return t.getClass().getName();
    }
}
