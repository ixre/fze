/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : RegistryMapper.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-05-28 14:24
 * description :
 * history :
 */
package net.fze.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jarrysix
 */
public class RegistryUtils {

    /**
     * 获取注册表对象所有键
     * @param object 对象
     * @return 键列表
     */
    public static List<String> getRegistryKeys(Object object) {
        List<String> ret = new ArrayList<>();
        //获取类型对象
        Class<?> classType = object.getClass();
        Field[] fields = classType.getDeclaredFields();
        for (Field field : fields) {
            //获取所有属性的访问权限
            field.setAccessible(true);
            RegistryKey annotation = field.getAnnotation(RegistryKey.class);
            if (annotation != null) {
                ret.add(annotation.value());
            }
        }
        return ret;
    }

    /**
     * 设置属性值
     * @param object 注册表对象
     * @param attributeName 属性名称
     * @param value 属性值
     */
    public static void setAttribute(Object object, String attributeName,String value){
        //获取类型对象
        Class<?> classType = object.getClass();
        Field[] fields = classType.getDeclaredFields();
        try {
            for (Field field : fields) {
                //获取所有属性的访问权限
                field.setAccessible(true);
                RegistryKey annotation = field.getAnnotation(RegistryKey.class);
                if(annotation == null){
                    continue;
                }
                if(attributeName.equals(annotation.value())){
                    field.set(object, value);
                    break;
                }
            }
        }catch (Throwable ex){
            throw new RuntimeException(ex);
        }
    }
}