package net.fze.util.tuple;

public class Tuple2<T1, T2> {
    private final T1 t1;
    private final T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getItem1() {
        return this.t1;
    }

    public T2 getItem2() {
        return this.t2;
    }
}
