package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 适配JPA规范的Mapper基础类型
 *
 * @param <P>
 * @param <T>
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
        if (pk == null || pk.equals("") || pk.equals(0)) {
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
        return Optional.of(this.selectOne(new QueryWrapper<>(o)));
    }

    /**
     * 根据对象条件查找
     */
    default List<T> findAll(T o) {
        return this.selectList(new QueryWrapper<>(o));
    }
}
