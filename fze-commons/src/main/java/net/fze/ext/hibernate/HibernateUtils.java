package net.fze.ext.hibernate;

import net.fze.util.TypeConv;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HibernateUtils {
     static Long getPkId(Object t) {
         // not works
        Method[] methods = t.getClass().getMethods();
        try {
            for (Method m : methods) {
                if (m.getAnnotation(Id.class) != null) {
                    Object v = m.invoke(t);
                    return TypeConv.toLong(v);
                }
            }
        }catch(Throwable ex){

        }
        return -1L;
    }
}
