package net.fze.domain;

/**
 * 仓库接口
 * @param <T>　聚合标识类型
 * @param <E> 实体
 */
public interface IRepository<T, E> {
    /**
     * 创建聚合
     */
    E create(T t);

    /**
     * 获取聚合
     */
    E get(T t);

    /**
     * 保存聚合值
     */
    void save(E e);

    /**
     * 删除聚合
     */
    void delete(E e);
}
