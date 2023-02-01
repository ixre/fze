package net.fze.common;

import net.fze.util.Strings;
import net.fze.util.TypeConv;
import net.fze.util.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 自定义标准库扩展
 */
public class Standard {
    private static int devFlag = -1;

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

    private static boolean java9OrLater = detectIsJava9OrLater();

    private static Boolean detectIsJava9OrLater() {
        String version = System.getProperty("java.version");
        if (Pattern.compile("^\\d+$").matcher(version).find()) {
            return TypeConv.toInt(version) >= 9;
        }
        return false;
    }

    public static boolean isJava9OrLater() {
        return java9OrLater;
    }

    public static boolean classInJar(Class<?> c) {
        // var className= Thread.currentThread().stackTrace[1].className;
        // val c = Class.forName(className)
        // var c = Typed::class.java

        // note: 将JDK定为1.8
        // val pkg = if (isJava9OrLater()) c.packageName else c.`package`.name
        String pkg = c.getPackage().getName();
        String resName = pkg.replace(".", "/");
        URL pkgPath = c.getClassLoader().getResource(resName);
        String path = pkgPath != null ? pkgPath.getPath() : "";
        return path.indexOf(".jar!") != -1
                || path.indexOf(".war!") != -1;
    }

    /** 解析环境,如果是生产环境返回true,反之返回false */
    public static boolean resolveEnvironment(Class<?> main) {

        devFlag = classInJar(main) ? 0 : 1;
        if (dev()) {
            // 在IDEA下开发时设置项目真实的工作空间
            String workspace = System.getProperty("user.dir");
            // Windows下以"\"分隔
            List<String> sep = new ArrayList<>();
            sep.add("/build");
            sep.add("/target");
            sep.add("\\build");
            sep.add("\\target");
            int i = Strings.indexOfAny(workspace, sep);
            if (i != -1) {
                System.setProperty("user.dir", workspace.substring(0, i));
            }
            return false;
        }
        return true;
    }

    /**
     * 是否为开发环境
     */
    public static boolean dev() {
        if (devFlag == -1)
            throw new Error("should call method Standard.resolveEnvironment first");
        return devFlag == 1;
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

    /** 捕获异常,执行操作 */
    public static <T> CatchResult<T> tryCatch(Supplier<T> p) {
        try {
            return new CatchResult(null, p.get());
        } catch (Throwable ex) {
            return new CatchResult(ex, null);
        }
    }

    /** 捕获错误 */
    public static <T> Error catchError(Supplier<T> p) {
        return tryCatch(p).error();
    }

    public static void setDefaultExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        // Thread.setDefaultUncaughtExceptionHandler { thread: Thread, throwable:
        // Throwable ->
        // if (throwable is NullPointerException) {
        // println("[ app][ exception]: thread ${thread.id},${throwable.message}");
        // throwable.printStackTrace()
        // }else{
        // throw throwable
        // }
        // }
    }
}
