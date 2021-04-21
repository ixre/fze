package net.fze.annotation

import java.lang.annotation.Inherited


/**
 * 资源
 */
@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Repeatable
@MustBeDocumented
annotation class Resource(val key:String,val name:String = "");
