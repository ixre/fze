package net.fze.service;

import net.fze.domain.IOrmRepository;

import javax.inject.Inject;

/**
 * 服务基类,使用注解@Inject装载
 *
 * @param <T>
 */
public abstract class BaseServiceImpl<T> extends AbstractServiceImpl<T> implements IBaseService<T> {
    @Inject
    IOrmRepository<T> r;

    @Override
    protected void onConfigure() {
        this.setRepository(r);
    }
}
