package net.fze.util.tuple;

public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
    private final T4 t4;

    Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        super(t1, t2, t3);
        this.t4 = t4;
    }

    public T4 getItem4() {
        return this.t4;
    }
}
