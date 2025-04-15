package net.fze.service;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.IOrmRepository;
import net.fze.domain.query.IQueryWrapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 服务基类,使用注解@Inject装载
 *
 * @param <T>
 */
public abstract class AbstractServiceImpl<T> implements IBaseService<T> {

    /**
     * 仓库对象
     */
    private IOrmRepository<T> r;

    protected abstract void onConfigure();

    private void check() {
        if (this.r == null) {
            this.onConfigure();
            if (this.r == null) {
                throw new RuntimeException("Not found repository, please check your configuration");
            }
        }
    }

    public IOrmRepository<T> getRepository() {
        this.check();
        return r;
    }

    protected void setRepository(IOrmRepository<T> o) {
        this.r = o;
    }

    @Override
    public T findById(Serializable id) {
        this.check();
        return r.findById(id);
    }

    @Override
    public void save(T e, Function<T, Serializable> f) {
        this.check();
        r.save(e, f);
    }

    @Override
    public T findBy(T o) {
        this.check();
        return r.findBy(o);
    }

    @Override
    public T findBy(IQueryWrapper o) {
        this.check();
        return r.findBy(o);
    }

    @Override
    public List<T> selectListBy(IQueryWrapper o) {
        this.check();
        List<T> list = r.selectListBy(o);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<T> selectListBy(T o) {
        this.check();
        List<T> list = r.selectListBy(o);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public PagingResult<T> selectPaging(IQueryWrapper o, PagingParams page) {
        this.check();
        return r.selectPaging(o, page);
//        throw new RuntimeException("未实现分页查询");
    }

    @Override
    public void saveAll(Iterable<T> entities, Function<T, Serializable> f) {
        this.check();
        entities.forEach(a -> this.save(a, f));
    }

    @Override
    public int deleteById(Serializable id) {
        this.check();
        return r.deleteById(id);
    }

    @Override
    public int batchDelete(List<Serializable> id) {
        this.check();
        return r.deleteBatchIds(id);
    }
}
