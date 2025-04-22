package net.fze.ext.mybatis;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实体工具
 */
public class BeanUtils {
    /**
     * 将源对象的属性值复制到目标对象，忽略源对象的 null 值属性。
     *
     * @param source 源对象，提供属性值。
     * @param target 目标对象，接收属性值。
     * @throws RuntimeException 如果在属性复制过程中出现异常。
     */
    public static void copyPropertiesIgnoreNull(Object source,Object target) throws RuntimeException {
        org.springframework.beans.BeanUtils.copyProperties(source,target,
                getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyProperties(Object source,Object target){
        org.springframework.beans.BeanUtils.copyProperties(source,target);
    }

    public static void copyProperties(Object source,Object target,String... ignoreFields){
        org.springframework.beans.BeanUtils.copyProperties(source,target,ignoreFields);
    }

}
