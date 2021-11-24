package net.fze.common

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.EmptyCoroutineContext

fun String?.isNullEmpty(): Boolean {
    return this == null || this == ""
}


// kotlin语言支持
open class KotlinLangExtension {

    fun regexp(pattern: String): Regex = Regex(pattern)
    fun regexp(pattern: String, opt: RegexOption): Regex = Regex(pattern, opt)

    /** 后台线程运行 */
    @Deprecated("use thread()", ReplaceWith("thread(start = true) { f.run() }", "kotlin.concurrent.thread"))
    fun threadRun(f: Runnable) {
        thread(start = true) {
            f.run()
        }
    }

    /** 后台线程运行 */
    fun thread(f: Runnable) {
        thread(start = true) {
            f.run()
        }
    }

    /** 使用协程运行 */
    fun coroutinesRun(block: Runnable): Job {
        return GlobalScope.launch(
            EmptyCoroutineContext,
            CoroutineStart.DEFAULT
        ) { block.run() }
    }

    /** 使用协程运行 */
    suspend fun <T> coroutines(
        isBlock: Boolean = false,
        block: suspend CoroutineScope.() -> T
    ) {
        return coroutineScope {
            if (isBlock) {
                runBlocking {
                    block()
                }
            }
            launch {
                block()
            }
        }
    }


    // 捕获异常,执行操作
    fun <T> tryCatch(p: () -> T?): CatchResult<T> {
        return typedCatch(p)
    }
}
