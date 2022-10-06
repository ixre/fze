package net.fze.util.tuple;

public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
    private final T3 t3;

    Tuple3(T1 t1, T2 t2, T3 t3) {
        super(t1, t2);
        this.t3 = t3;
    }

    public T3 getItem3() {
        return this.t3;
    }
}
