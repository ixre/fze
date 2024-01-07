package net.fze.util;

import org.junit.jupiter.api.Test;

class MathUtilsTest {
    @Test
    void test1(){
        int i1 = MathUtils.maxDivisor(8,15);
        int i2 = MathUtils.maxDivisor(9,17);
        int i3 = MathUtils.maxDivisor(27,9);
        System.out.println(i1+"/"+i2+"/"+i3);
    }
}