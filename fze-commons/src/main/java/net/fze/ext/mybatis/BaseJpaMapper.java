package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.IOrmRepository;
import net.fze.domain.query.IQueryWrapper;
import net.fze.util.TypeConv;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 适配JPA规范的Mapper基础类型
 *
 * @param <T> 实体
 * @author jarrysix
 */
public interface BaseJpaMapper<T> extends BaseMapper<T>, IOrmRepository<T> {
    /**
     * 根据主键查找
     */
    default T findById(Serializable id) {
        return this.selectById(id);
    }

    /**
     * 保存实体
     */
    default void save(T e, Function<T, Serializable> f) {
        Object pk = f.apply(e);
        if (pk == null || "".equals(pk) || TypeConv.toFloat(pk) <= 0) {
            // 自增主键应该添加生成策略,才能返回主键. 如:
            //  @TableId(value = "id", type = IdType.AUTO)
            //  private Long id;
            this.insert(e);
        } else {
            this.updateById(e);
        }
    }

    /**
     * 根据对象条件查找
     */
    default T findBy(T o) {
        return this.selectOne(new QueryWrapper<>(o));
    }

    /**
     * 根据查询条件查找
     */
    default T findBy(IQueryWrapper queryWrapper) {
        assert queryWrapper instanceof Wrapper;
        @SuppressWarnings("unchecked")
        T t = this.selectOne((Wrapper<T>) queryWrapper);
        return t;
    }

    default long count(IQueryWrapper queryWrapper) {
        assert queryWrapper instanceof Wrapper;
        @SuppressWarnings("unchecked")
        long count = this.selectCount((Wrapper<T>) queryWrapper);
        return count;
    }

    /**
     * 根据对象条件查找
     */
    default List<T> selectListBy(T o) {
        return this.selectList(new QueryWrapper<>(o));
    }

    default List<T> selectListBy(IQueryWrapper queryWrapper) {
        assert queryWrapper instanceof Wrapper;
        @SuppressWarnings("unchecked")
        List<T> list = this.selectList((Wrapper<T>) queryWrapper);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }


    /**
     * 分页查询
     * // var queryWrapper = new QueryWrapper();
     * //       if(!Strings.isNullOrEmpty(keywords)) {
     * //           queryWrapper.like("name", "%" + keywords + "%");
     * //       }
     * //       if(!Strings.isNullOrEmpty(orderBy)){
     * //           queryWrapper.orderByDesc(orderBy);
     * //      }
     * //      PagingResult pageResult = this.repo.queryPagedData(queryWrapper, pageNum,pageSize);
     * //     return pageResult;
     *
     * @param query  查询条件
     * @param params 分页参数
     * @return 分页数据
     */
    default PagingResult<T> selectPaging(IQueryWrapper query, PagingParams params) {
        assert query != null;
        assert query instanceof QueryWrapper;
        @SuppressWarnings("unchecked")
        Wrapper<T> t = (Wrapper<T>) query;
        // 获取总条数
        long count = this.selectCount(t);
        // 转换分页参数
        IPage<T> p = new Page<>(params.getPageIndex(), params.getPageSize(), count);
        IPage<T> page = this.selectPage(p, t);
        return new PagingResult<>(count, page.getRecords());
    }


    /**
     * 分页查询
     * // var queryWrapper = new QueryWrapper();
     * //       if(!Strings.isNullOrEmpty(keywords)) {
     * //           queryWrapper.like("name", "%" + keywords + "%");
     * //       }
     * //       if(!Strings.isNullOrEmpty(orderBy)){
     * //           queryWrapper.orderByDesc(orderBy);
     * //      }
     * //      PagingResult pageResult = this.repo.queryPagedData(queryWrapper, pageNum,pageSize);
     * //     return pageResult;
     *
     * @param query  查询条件
     * @param params 分页参数
     * @return 分页数据
     */
    default PagingResult<T> queryPaging(Wrapper<T> query, PagingParams params) {
        // 获取总条数
        long count = this.selectCount(query);
        // 转换分页参数
        IPage<T> p = new Page<>(params.getPageIndex(), params.getPageSize(), count);
        IPage<T> page = this.selectPage(p, query);
        return new PagingResult<>(count, page.getRecords());
    }
}
