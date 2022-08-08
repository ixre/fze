package net.fze.util;

import org.junit.jupiter.api.Test;

class MathUtilTest {
    @Test
    void test1(){
        int i1 = MathUtil.maxDivisor(8,15);
        int i2 = MathUtil.maxDivisor(9,17);
        int i3 = MathUtil.maxDivisor(27,9);
        System.out.println(i1+"/"+i2+"/"+i3);
    }
}