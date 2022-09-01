package net.fze.util;

/**
 * 断言
 */
public class Assert {
    public static void isNullOrEmpty(Object v,String message){
        test(v == null || v.equals(""), Types.orValue(message,"argument is null"));
    }

    public static void test(Boolean b,String message){
        if(!b)throw new IllegalArgumentException(Types.orValue(message,"assert failed"));
    }
}
