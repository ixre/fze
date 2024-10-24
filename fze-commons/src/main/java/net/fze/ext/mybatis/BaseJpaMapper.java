package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.fze.util.TypeConv;

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
}
