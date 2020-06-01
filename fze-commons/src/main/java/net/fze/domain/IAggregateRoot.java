package net.fze.domain;

/** 聚合根 */
public interface IAggregateRoot<T> {
    /**
     * 获取聚合根编号
     *
     * @return 编号
     */
    T getAggregateRootId();
}
