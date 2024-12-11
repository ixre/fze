package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.fze.common.data.PagingResult;
import net.fze.util.TypeConv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 适配JPA规范的Mapper基础类型
 *
 * @author jarrysix
 * @param <P> 主键
 * @param <T> 实体
 */
public interface BaseJpaMapper<P extends Serializable, T> extends BaseMapper<T> {
    /**
     * 根据主键查找
     */
    default Optional<T> findById(P id) {
        return Optional.of(this.selectById(id));
    }

    /**
     * 保存实体
     */
    default T save(T e, Function<T, P> f) {
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
     *
     *  // var queryWrapper = new QueryWrapper();
     *  //       if(!Strings.isNullOrEmpty(keywords)) {
     *  //           queryWrapper.like("name", "%" + keywords + "%");
     *  //       }
     *  //       if(!Strings.isNullOrEmpty(orderBy)){
     *  //           queryWrapper.orderByDesc(orderBy);
     *   //      }
     *   //      Pageable pageable = PageRequest.of(pageNum, pageSize);
     *   //      PagingResult pageResult = this.repo.queryPagedData(queryWrapper, pageable);
     *    //     return pageResult;
     * @param query
     * @param pageable
     * @return
     */
    default PagingResult<T> queryPagedData(QueryWrapper<T> query, Pageable pageable) {
        throw new UnsupportedOperationException("未实现分页查询");
//        // 获取总条数
//        long count = this.selectCount(query);
//        // 转换分页参数
//        Page<T> p = new Paging<>(pageable.getPageNumber(), pageable.getPageSize(), count);
//        Page<T> page = this.selectPage(p, query);
//        return new PagingResult<T>(count, page.getRecords());
    }
}
