package net.fze.domain;

class EventBusGetTypeTest<T> {
    private T t;
    public EventBusGetTypeTest(T t) {
        this.t = t;
    }
    public void test() {
        System.out.println(t.getClass());
    }
}
