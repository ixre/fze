package net.fze.service;

import net.fze.domain.IOrmRepository;
import net.fze.ext.mybatis.BaseJpaMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring服务基类,与BaseServiceImpl不同的是,这里使用了@Autowired注解，能根据泛型类型注入
 *
 * @param <T>
 */
public abstract class SpringBaseServiceImpl<T> extends AbstractServiceImpl<T> implements IBaseService<T> {
    @Autowired
    IOrmRepository<T> r;

    public BaseJpaMapper<T> getMapper() {
        if (r instanceof BaseJpaMapper) {
            return (BaseJpaMapper<T>) r;
        }
        throw new RuntimeException("仓库类未继承: BaseJpaMapper<T>");
    }

    @Override
    protected void onConfigure() {
        this.setRepository(r);
    }
}
