package net.fze.ext.rbac.scope;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataScope {
    String departField();

    String userField();
}
