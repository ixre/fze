package net.fze.common

import java.util.*

/**
 * 捕获异常,将异常返回错误
 *
 * Kotlin: val err = catch{ 5/0 }
 * Java: Error err = Typed.std.catch(()->5/0);
 *
 */
fun <T> catch(p: () -> T?): CatchResult<T> {
    return typedCatch(p)
}


/** 可空类型应用回调 */
fun <T> T?.apply(call: (T) -> Unit):T?{
    if(this != null)call(this)
    return this
}

/** 往字典里插入数据,并返回实例 */
fun <K,V> MutableMap<K,V>.append(k:K,v:V):MutableMap<K,V>{
    this[k] = v
    return this
}


/**
 * 捕获异常,返回T,输出错误信息
 *
 * Kotlin: val err = typedCatch{ Error("message") }
 * Java: Error err = Typed.std.typedCatch(()->Error("message"));
 */

internal fun <T> typedCatch(p: () -> T?): CatchResult<T> {
    return try {
        CatchResult(null, p())
    } catch (ex: Throwable) {
        CatchResult(ex, null)
    }
}
