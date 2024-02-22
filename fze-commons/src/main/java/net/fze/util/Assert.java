package net.fze.util;

/**
 * 断言
 */
public class Assert {
    /**
     * 断言类型
     * @param v 值
     * @param clazz 类型
     */
    public static void checkType(Object v,Class<?> clazz){
        if(v.getClass() != clazz && !clazz.isAssignableFrom(v.getClass())){
            // 类型不同，且非父类和接口类，则抛出异常
            throw new RuntimeException(String.format("type not match %s",clazz.toString()));
        }
    }
    public static void isNullOrEmpty(Object v, String message) {
        test(!(v == null || "".equals(v)), Types.orValue(message, "argument is null"));
    }

    public static void test(Boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(Types.orValue(message, "assert failed"));
        }
    }
}
