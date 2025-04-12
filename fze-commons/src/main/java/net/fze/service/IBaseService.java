package net.fze.service;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 基础服务接口
 */
public interface IBaseService<T> {
    /** 根据主键查找 */
    T findById(Serializable id);

    /** 查找全部 */
    List<T> findAll();

    /** 保存实体 */
    void save(T e, Function<T,Serializable> f);

    /** 根据对象条件查找 */
    T findBy(T o);

    /** 根据对象条件查找 */
    List<T> selectListBy(T o);

    /** 根据条件分页查询 */
    PagingResult<T> selectPaging(T o, PagingParams page);

    /** 批量保存 */
    void saveAll(Iterable<T> entities,Function<T,Serializable> f);

    /** 根据主键删除 */
    int deleteById(Serializable id);

    /** 批量删除国家 */
    int batchDelete(List<Serializable> id);
}
