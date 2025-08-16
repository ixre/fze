package net.fze.service;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.IOrmRepository;
import net.fze.domain.query.IQueryWrapper;
import net.fze.jdk.jdk8.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<T> findByIds(Collection<?> ids) {
        this.check();
        List<Serializable> list = ids.stream()
                .map(a -> {
                    assert a instanceof Serializable;
                   return (Serializable) a;
                }).collect(Collectors.toList());
        return r.findByIds(list);
    }

    @Override
    public void insert(T e) {
        this.check();
        r.save(e, a -> null);
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
    public long count(IQueryWrapper o) {
        this.check();
        return r.count(o);
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
    }

    @Override
    public PagingResult<?> selectCustomPaging(String sql, IQueryWrapper o,PagingParams page) {
        this.check();
        return r.selectCustomPaging(sql,o,page);
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


    /**
     * 批量获取实体并转换为 Map
     * @param ids 实体 ID 列表
     * @param idGetter 获取实体 ID 的函数
     * @return 实体 ID 到实体转换后的 Map 的映射
     */
    @Override
    public <P> Map<P,T> selectAsMap(List<P> ids, Function<T, P> idGetter) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return this.findByIds(ids).stream()
                .collect(Collectors.toMap(
                        idGetter,
                        entity -> entity
                ));
    }

    /**
     * 从源实体列表中提取 ID 列表，批量获取目标实体并转换为 Map
     * @param sourceEntities 源实体列表
     * @param sourceExtractor 从源实体中提取 ID 的函数
     * @param dstExtractor 获取目标实体 ID 的函数
     * @param <S> 源实体类型
     * @return 目标实体 ID 到目标实体转换后的 Map 的映射
     */
    @Override
    public <S,P> Map<P, T> selectAsMap(List<S> sourceEntities, Function<S, P> sourceExtractor, Function<T, P> dstExtractor) {
        List<P> ids = sourceEntities.stream()
                .map(sourceExtractor)
                .collect(Collectors.toList());
        return selectAsMap(ids, dstExtractor);
    }
}
