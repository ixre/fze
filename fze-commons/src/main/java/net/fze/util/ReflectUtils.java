package net.fze.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 反射工具类
 *
 * @author jarrysix
 */
public class ReflectUtils {
    /**
     * 使用反射拷贝对象
     * @param raw 原对象
     * @param target 目标对象
     * @param <T> 类型
     */
    public static <T> void copyObject(T raw,T target) {
        //获取类型对象
        Class<?> classType = raw.getClass();
        Field[] fields = classType.getDeclaredFields();
        List<Method> methods = Arrays.asList(classType.getMethods());
        try {
            for (Field field : fields) {
                //获取所有属性的访问权限
                field.setAccessible(true);
                //获取字段名称，根据字段名称生成对应的方法名，用于反射
                String fieldName = field.getName();
                String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                String getStr = "get" + methodName;
                String setStr = "set" + methodName;
                //调用原对象的get方法为拷贝对象的set方法赋值
                Method getMethod = methods.stream().filter(a->getStr.equals(a.getName())).findFirst().orElse(null);
                if(getMethod == null){
                    //如果找不到get方法，则尝试使用is开头的方法
                    getMethod = methods.stream().filter(a->("is"+methodName).equals(a.getName()) && a.getParameterCount() == 0).findFirst().orElse(null);
                }
                Method setMethod = methods.stream().filter(a->setStr.equals(a.getName())).findFirst().orElse(null);
                if(setMethod == null){
                    //如果找不到set方法，则尝试使用is开头的方法
                    setMethod = methods.stream().filter(a->("is"+methodName).equals(a.getName()) && a.getParameterCount() == 1).findFirst().orElse(null);
                }
                if(getMethod == null || setMethod == null){
                    //如果找不到get或者set方法，则跳过
                    continue;
                }
                Object getMethodResult = getMethod.invoke(raw);
                //调用set方法把原对象的值复制到拷贝对象
                setMethod.invoke(target, getMethodResult);
            }
        }catch (Throwable ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}