package net.fze.domain;

/**
 * 仓库接口
 *
 * @param <P> 　聚合标识类型
 * @param <A> 聚合根接口类型
 * @param <E> 实体类型
 */
public interface IRepository<P,A,E> {
    /**
     * 创建聚合
     */
    A create(E t);

    /**
     * 获取聚合
     */
    A get(P t);

    /**
     * 保存聚合值
     */
    P save(E e);

    /**
     * 删除聚合
     */
    void delete(E e);
}
