package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.IOrmRepository;
import net.fze.util.TypeConv;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 适配JPA规范的Mapper基础类型
 *
 * @author jarrysix
 * @param <T> 实体
 */
public interface BaseJpaMapper<T> extends BaseMapper<T>, IOrmRepository<T> {
    /**
     * 根据主键查找
     */
    default Optional<T> findById(Serializable id) {
        T t = this.selectById(id);
        return t == null? Optional.empty():Optional.of(t);
    }

    /**
     * 保存实体
     */
    default T save(T e, Function<T, Serializable> f) {
        Object pk = f.apply(e);
        if (pk == null || "".equals(pk) || TypeConv.toFloat(pk) <= 0) {
            // 自增主键应该添加生成策略,才能返回主键. 如:
            //  @TableId(value = "id", type = IdType.AUTO)
            //  private Long id;
            this.insert(e);
        } else {
            this.updateById(e);
        }
        return e;
    }

    /**
     * 根据对象条件查找
     */
    default Optional<T> findOne(T o) {
        T v=  this.selectOne(new QueryWrapper<>(o));
        if (v == null) {
            return Optional.empty();
        }
        return Optional.of(v);
    }

    /**
     * 根据对象条件查找
     */
    default List<T> findBy(T o) {
        return this.selectList(new QueryWrapper<>(o));
    }


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
     * @param params 分页参数
     * @return 分页数据
     */
    default PagingResult<T> selectPaging(Object query, PagingParams params) {
        assert query != null;
       @SuppressWarnings("unchecked")
        T t = (T)query;
        // 获取总条数
        long count = this.selectCount(new QueryWrapper<T>(t));
        // 转换分页参数
        IPage<T> p = new Page<>(params.getPageIndex(),params.getPageSize(), count);
        IPage<T> page = this.selectPage(p, new QueryWrapper<>(t));
        return new PagingResult<T>(count, page.getRecords());
    }


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
     * @param params 分页参数
     * @return 分页数据
     */
    default PagingResult<T> queryPaging(QueryWrapper<T> query, PagingParams params) {
        // 获取总条数
        long count = this.selectCount(query);
        // 转换分页参数
        IPage<T> p = new Page<>(params.getPageIndex(),params.getPageSize(), count);
        IPage<T> page = this.selectPage(p, query);
        return new PagingResult<T>(count, page.getRecords());
    }
}
