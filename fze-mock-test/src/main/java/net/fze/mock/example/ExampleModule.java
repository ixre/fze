package net.fze.mock.example;

import net.fze.mock.MockContextImpl;
import com.google.inject.Binder;
import com.google.inject.Module;
import net.fze.commons.Context;

/**
 * 服务初始化
 */
class ExampleModule implements Module {
    /**
     * 注册类型
     *
     * @param binder 注入绑定
     */
    @Override
    public void configure(Binder binder) {
        // 注入基础对象
        this.injectContext(binder);
        // 注入仓储实现
        this.injectRegister(binder);
    }

    /**
     * 注入上下文
     *
     * @param binder 绑定器
     */
    private void injectContext(Binder binder) {
        binder.bind(Context.class).to(MockContextImpl.class);
    }

    /**
     * 依赖注入注册
     *
     * @param binder 绑定器
     */
    private void injectRegister(Binder binder) {
        // 注册服务实现
        binder.bind(StatusService.class).to(MockStatusServiceImpl.class);
    }
}
