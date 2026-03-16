package net.fze.ext.rbac.scope;

import java.lang.annotation.*;

/**
 * 数据范围注解
 * @author jarrysix
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataScope {
    String departField() default "";

    String userField() default "";
}
