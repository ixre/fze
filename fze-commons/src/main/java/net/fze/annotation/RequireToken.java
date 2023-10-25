package net.fze.annotation;


import java.lang.annotation.*;

/**
 * 需要请求令牌,例如JWT
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // 需在Runtime中保持,不然拦劫时识别不了
@Inherited
public @interface RequireToken {
    /**
     * 是否必须,默认:是
     */
    boolean required() default true;
}

