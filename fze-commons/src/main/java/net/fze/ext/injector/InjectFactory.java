package net.fze.ext.injector;

/**
 * 注入工厂
 * @author jarrysix
 */
public class InjectFactory {

    private static Injector injectorInstance;

    /**
     * 设置注入器
     * ```java
     * InjectFactory.configure(new Injector() {
     *
     * @param injector 注入器
     * return null;
     * }
     * });
     * ```
     */
    public static void configure(Injector injector) {
        injectorInstance = injector;
    }

    /**
     * 获取实例
     *
     * @param clazz 类型
     * @return 实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        if (injectorInstance == null) {
            throw new IllegalArgumentException("依赖注入器未初始化");
        }
        return injectorInstance.getInstance(clazz);
    }
}
