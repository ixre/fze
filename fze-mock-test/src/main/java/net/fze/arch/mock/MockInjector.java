package net.fze.arch.mock;


import com.google.inject.Injector;

class MockInjector implements net.fze.arch.commons.Injector {
    private final Injector _injector;

    MockInjector(Injector i) {
        this._injector = i;
    }

    /**
     * 获取服务
     *
     * @param c   服务类型
     * @param <T> 服务
     * @return 服务
     */
    public <T> T getInstance(Class<T> c) {
        return this._injector.getInstance(c);
    }
}
