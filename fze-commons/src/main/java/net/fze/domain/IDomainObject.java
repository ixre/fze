package net.fze.domain;

/**
 * 领域对象
 */
public interface IDomainObject<T> {
    /**
     * 获取领域对象编号
     *
     * @return 编号
     */
    T getDomainId();
}
