/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : RegistryKey.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-05-28 14:21
 * description :
 * history :
 */
package net.fze.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注册表注解
 * @author jarrysix
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistryKey {
    /**
     * 注册表key
     */
    String value() default "";
}
