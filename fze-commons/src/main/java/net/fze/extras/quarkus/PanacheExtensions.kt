package net.fze.extras.quarkus

import io.quarkus.hibernate.orm.panache.PanacheQuery
import net.fze.jdk.value

/** 返回单个对象 */
fun <T> PanacheQuery<T>.single():T?{
   return this.singleResultOptional<T>().value()
}