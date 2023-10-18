package net.fze.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 资源注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Resource {
    String key() default "";

    String name() default "";
}
