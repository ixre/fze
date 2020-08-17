package net.fze.commons.std

import kotlinx.coroutines.*
import net.fze.commons.CatchResult
import net.fze.util.typedCatch
import java.lang.Runnable
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun String?.isNullEmpty(): Boolean {
    return this == null || this.isEmpty()
}


// kotlin语言支持
open class KotlinLangExtension {
    val letterStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    fun template(text: String?, args: Map<String, String>): String {
        if (text == null) return ""
        val re = Regex("\\{([^{]+?)}")
        return re.replace(text) {
            args[it.groups[1]!!.value]?:it.value
        }
    }

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
        ) { block.run()}
    }

    /** 使用协程运行 */
    fun coroutines(context: CoroutineContext = EmptyCoroutineContext,
                  start: CoroutineStart = CoroutineStart.DEFAULT,
                  block: suspend CoroutineScope.() -> Unit):Job {
        return GlobalScope.launch(context, start, block)
    }

    // 返回随机字符串,[n]:长度
    fun randomLetters(n: Int, letters: String = letterStr): String {
        val l = letters.length
        var arr = arrayOfNulls<Char?>(n)
        var rd = java.util.Random()
        for (i in 0 until n) {
            arr[i] = letters[rd.nextInt(l) % l]
        }
        return arr.joinToString("")
    }

    // 获取字符串味之素

    fun endPosition(s: String, b: Int, n: Int): Int {
        if (n == -1) {
            return s.length
        }
        return b + n
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    fun replaceRange(s: String, b: Int, n: Int, replace: String): String {
        val end = this.endPosition(s, b, n)
        return s.replaceRange(b, end, replace)
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    fun replaceN(s: String, b: Int, n: Int, replace: String): String {
        val end = this.endPosition(s, b, n)
        return s.replaceRange(b, end, this.stringRepeat(replace, end - b))
    }

    // 重复字符串
    fun stringRepeat(s: String, n: Int): String {
        var arr = arrayOfNulls<String>(n)
        arr.forEachIndexed { i, _ -> arr[i] = s }
        return arr.joinToString("")
    }

    // 捕获异常,执行操作
    fun <T> tryCatch(p: () -> T?): CatchResult<T> {
        return typedCatch(p)
    }
}
