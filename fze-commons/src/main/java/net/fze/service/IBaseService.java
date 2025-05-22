package net.fze.service;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.query.IQueryWrapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基础服务接口
 */
public interface IBaseService<T> {
    /**
     * 根据主键查找
     */
    T findById(Serializable id);

    /**
     * 根据主键根据多个实体
     */
    List<T> findByIds(Collection<?> ids);
    /**
     * 保存实体
     */
    void save(T e, Function<T, Serializable> f);

    /**
     * 根据对象条件查找
     */
    T findBy(T o);

    /**
     * 根据对象条件查找
     */
    T findBy(IQueryWrapper o);

    /**
     * 根据查询条件统计数量
     */
    long count(IQueryWrapper query);

    /**
     * 根据对象条件查找
     */
    List<T> selectListBy(T o);

    /**
     * 根据条件查询
     */
    List<T> selectListBy(IQueryWrapper o);

    /**
     * 根据条件分页查询
     */
    PagingResult<T> selectPaging(IQueryWrapper o, PagingParams page);

    /**
     * 批量保存
     */
    void saveAll(Iterable<T> entities, Function<T, Serializable> f);

    /**
     * 根据主键删除
     */
    int deleteById(Serializable id);

    /**
     * 批量删除国家
     */
    int batchDelete(List<Serializable> id);

    /**
     * 批量获取实体并转换为 Map
     * @param ids 实体 ID 列表
     * @param idGetter 获取实体 ID 的函数
     * @return 实体 ID 到实体转换后的 Map 的映射
     */
     <P> Map<P,T> selectAsMap(List<P> ids, Function<T, P> idGetter);

     /**
     * 从源实体列表中提取 ID 列表，批量获取目标实体并转换为 Map
     * @param sourceEntities 源实体列表
     * @param sourceExtractor 从源实体中提取 ID 的函数
     * @param dstExtractor 获取目标实体 ID 的函数
     * @param <S> 源实体类型
     * @return 目标实体 ID 到目标实体转换后的 Map 的映射
     */
    <S,P> Map<P, T> selectAsMap(List<S> sourceEntities, Function<S, P> sourceExtractor, Function<T, P> dstExtractor);
}
