package net.fze.domain;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.query.IQueryWrapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * ORM映射仓库
 * @param <T> 实体类型
 */
public interface IOrmRepository<T> {
    /**
     * 根据主键查找
     */
    T findById(Serializable id);

    /**
     * 根据主键查找列表
     */
    List<T> findByIds(List<Serializable> ids);
    /**
     * 根据主键查找
     */
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
    T findBy(IQueryWrapper query);

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
     * @param query 查询条件
     * @return 查询结果
     */
    List<T> selectListBy(IQueryWrapper query);
    /**
     * 删除数据，返回影响行数
     */
    int deleteById(Serializable id);

    /**
     * 批量删除数据，返回影响行数
     */
    int deleteBatchIds(List<Serializable> ids);
    /**
     * 分页查询
     *  // var queryWrapper = new QueryWrapper();
     *  //       if(!Strings.isNullOrEmpty(keywords)) {
     *  //           queryWrapper.like("name", "%" + keywords + "%");
     *  //       }
     *  //       if(!Strings.isNullOrEmpty(orderBy)){
     *  //           queryWrapper.orderByDesc(orderBy);
     *   //      }
     *   //      PagingResult pageResult = this.repo.queryPagedData(queryWrapper, pageNum,pageSize);
     *    //     return pageResult;
     * @param query 查询条件
     * @param page 分页参数
     * @return 分页数据
     */
    PagingResult<T> selectPaging(IQueryWrapper query, PagingParams page);
}
