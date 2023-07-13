package net.fze.domain;

class EventBusGetTypeTest<T> {
    private T t;

    public EventBusGetTypeTest(T t) {
        this.t = t;
    }

    public void test() {
        EventBus.getDefault().asyncSubscribe(EventBusTest.Test2Event.class, (event) -> {
            System.out.println("--- ${it.name}");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.getClass());
    }
}
