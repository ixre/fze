package net.fze.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

/**
 * 需要请求令牌,例如JWT
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RequireToken {
    /**
     * 是否必须,默认:是
     */
    boolean required() default true;
}

