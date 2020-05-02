package com.github.ixre.fze.commons.std

// 异常结果
class CatchResult<T> {
    private var ex: Throwable? = null
    private var t: T? = null
    private var excepted = false

    internal constructor(ex: Throwable?, t: T?) {
        this.ex = ex
        this.t = t
    }

    // 处理错误
    fun except(p: (err: Throwable) -> Unit): CatchResult<T> {
        this.excepted = true
        if (this.ex != null) p(this.ex!!)
        return this
    }

    // 获取错误
    fun error(): Error? {
        if (this.ex != null) return Error(this.ex!!.message, this.ex)
        if (this.t is Error) return this.t as Error
        if (this.t is Throwable) {
            val ta = this.t as Throwable
            return Error(ta.message, ta)
        }
        return null
    }

    // 执行操作
    fun then(p: (t: T?) -> Unit): CatchResult<T> {
        this.throws()
        p(this.t)
        return this
    }

    // 如果没有值,返回默认d
    fun or(d: T): T {
        this.throws()
        return this.t ?: d
    }

    // 返回值
    fun unwrap(): T? {
        this.throws()
        return this.t
    }

    // 抛出异常
    private fun throws() {
        if (this.ex != null && !this.excepted) throw this.ex!!
    }
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

