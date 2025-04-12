package net.fze.domain;

import net.fze.common.data.PagingResult;

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
    Optional<T> findById(Serializable id);

    /**
     * 保存实体
     */
    T save(T e, Function<T, Serializable> f);

    /**
     * 根据对象条件查找
     */
    Optional<T> findOne(T o);

    /**
     * 根据对象条件查找
     */
    List<T> findBy(T o);

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
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页数据
     */
    PagingResult<T> selectPaging(Object query, int pageNum, int pageSize);
}
