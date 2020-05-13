package net.fze.domain;

/**
 * 值对象
 */
public interface IValueObject {
    /**
     * 值对象是否相同
     *
     * @param src    源对象
     * @param target 目标对象
     * @return 是或否
     */
    boolean equals(Object src, Object target);
}
