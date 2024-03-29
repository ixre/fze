package net.fze.util;

import net.fze.util.tuple.Tuple;
import net.fze.util.tuple.Tuple2;
import net.fze.util.tuple.Tuple3;
import net.fze.util.tuple.Tuple4;
import org.junit.jupiter.api.Test;

class TupleTest {

    @Test
    void testTuple(){
        Tuple2<Integer, String> t2 =  Tuple.of(1, "2");
        Tuple3<Integer, String,Boolean> t3 =  Tuple.of(1, "2", true);
        Tuple4<Integer, String, Double, Long> integerStringDoubleLongTuple4 = Tuple.of(1, "2", 3.5, 1L);
        System.out.println(t3.getItem1());
    }
}
