package net.fze.commons.std;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义标准库扩展
 */
public class Standard {
    /**
     * 标准库扩展
     */
    public static final LangExtension std = new LangExtension();
    /**
     * 编码器
     */
    public static final EncoderExtensions encoder = new EncoderExtensions();

    /**
     * 类型解析器
     */
    public static ClassResolver classResolver = new ClassResolver();


    public static boolean classInJar(Class<?> c) {
        return StandardKt.Companion.classInJar(c);
    }

    /**
     * 解析环境
     */
    public static boolean resolveEnvironment(Class<?> main) {
        return StandardKt.Companion.resolveEnvironment(main);
    }

    /**
     * 是否为开发环境
     */
    public static boolean dev() {
        return StandardKt.Companion.dev();
    }


    /**
     * 获取包下所有的类型
     *
     * @param pkg    包名
     * @param filter 筛选符合条件的类型，可以为空
     */
    public static Class<?>[] getPkgClasses(String pkg, Types.TCond<Class<?>> filter) {
        return classResolver.getClasses(pkg, filter);
    }

    public static Type getActualType(Object o, int index) {
        Type clazz = o.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) clazz;
        return pt.getActualTypeArguments()[index];
    }
}
