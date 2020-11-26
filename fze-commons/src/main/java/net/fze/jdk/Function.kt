package net.fze.jdk

import java.util.*

/** 扩展Optional安全的获取值 */
fun <T> Optional<T>.value():T?{
    if(this.isPresent)return this.get()
    return null
}