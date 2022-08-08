package net.fze.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 执行时请求加锁
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
    /**
     * 锁键
     * @return
     */
    String value();

    /**
     * 过期时间(秒)
     * @return
     */
    int expires() default 0;

    /**
     * 唯一编号,用于确定是否执行同一个批次,默认为空
     * @return
     */
    String id() default "";

    /** 延迟解锁, 以避免时间不同步时快速解锁导致的问题 */
    int delayUnlock() default 0;
}
