package net.fze.annotation;


import javax.management.relation.RoleUnresolved;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 上下文
 * @author jarrysix
 */
@Target({ ElementType.PARAMETER,  ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Context { }
