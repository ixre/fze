package net.fze.util;

import net.fze.ext.mybatis.BeanUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanUtilsTest {

    class A {
        private String a;
        private Integer b;
        public String getA() {
            return a;
        }
        public void setA(String a) {
            this.a = a;
        }
        public Integer getB() {
            return b;
        }
        public void setB(int b) {
            this.b = b;
        }
    }
    @Test
    void copyProperties() throws Exception {
        A a = new A();
        a.setA("a");
        A b = new A();
        b.setB(20);
        BeanUtils.copyProperties(a, b);
        assertEquals(a.getA(), b.getA());
        assertNotNull(b.getB());
    }
}