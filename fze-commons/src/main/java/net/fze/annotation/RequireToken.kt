package net.fze.annotation

import java.lang.annotation.Inherited

/**
 * 需要请求令牌,例如JWT
 */
@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@MustBeDocumented
@Inherited
annotation class RequireToken(val required:Boolean=true);

