package net.fze.ext.hibernate;

import net.fze.util.TypeConv;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TinySessionTest {


    @Test
    void saveOrUpdate() {
        TestUser tu = new TestUser();
        System.out.println(HibernateUtils.getPkId(tu));
    }

}