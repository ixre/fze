package net.fze.ext.injector;

/**
 * 注入工厂
 */
public class InjectFactory {

    private static Injector _injector;

    /**
     * 设置注入器
     * ```java
     * InjectFactory.configure(new Injector() {
     *
     * @param injector 注入器
     * @Override public <T> T getInstance(Class<T> c) {
     * return null;
     * }
     * });
     * ```
     */
    public static void configure(Injector injector) {
        _injector = injector;
    }

    /**
     * 获取实例
     *
     * @param clazz 类型
     * @return 实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        if (_injector == null) {
            throw new IllegalArgumentException("依赖注入器未初始化");
        }
        return _injector.getInstance(clazz);
    }
}
