package net.fze.common;

import net.fze.util.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义标准库扩展
 */
public class Standard {
  
    /**
     * 类型解析器
     */


    public static boolean classInJar(Class<?> c) {
        return Standard.classInJar(c);
    }

    

    

    public static Type getActualType(Object o, int index) {
        Type clazz = o.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) clazz;
        return pt.getActualTypeArguments()[index];
    }
}
