package net.fze.annotation


@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Repeatable
@MustBeDocumented
annotation class Resource(val key:String,val name:String = "");
