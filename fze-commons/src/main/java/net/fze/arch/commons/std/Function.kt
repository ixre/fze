package net.fze.arch.commons.std

/** 是否开发环境 */
fun dev():Boolean{
    return Standard.dev()
}


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
