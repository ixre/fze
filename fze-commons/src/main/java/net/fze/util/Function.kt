package net.fze.util

import net.fze.commons.CatchResult
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

/** 扩展Optional安全的获取值 */
fun <T> Optional<T>.value():T?{
    if(this.isPresent)return this.get()
    return null
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
