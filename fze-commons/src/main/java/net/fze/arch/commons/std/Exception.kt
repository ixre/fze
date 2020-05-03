package net.fze.arch.commons.std

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

