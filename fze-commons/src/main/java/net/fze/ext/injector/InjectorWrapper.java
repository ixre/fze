package net.fze.ext.injector;

/**
 * @author jarrysix
 */
public class InjectorWrapper implements Injector {
    private final com.google.inject.Injector _injector;

    public InjectorWrapper(com.google.inject.Injector i) {
        this._injector = i;
    }

    /**
     * 获取实例
     *
     * @param c 实例类型
     * @return 实例
     */
    @Override
    public <T> T getInstance(Class<T> c) {
        return this._injector.getInstance(c);
    }
}
