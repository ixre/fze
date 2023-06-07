package net.fze.lib.injector;

/**
 * 注入工厂
 */
public class InjectFactory {

    private static Injector _injector;

    /**
     * 设置注入器
     * @param injector 注入器
     */
    public static void configure(Injector injector) {
        _injector = injector;
    }

    /**
     * 获取实例
     * @param clazz 类型
     * @return 实例
     */
    public <T> T getInstance(Class<T> clazz) {
        return _injector.getInstance(clazz);
    }
}
