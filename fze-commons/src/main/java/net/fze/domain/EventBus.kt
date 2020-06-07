package net.fze.domain
import net.fze.util.catch

/** 事件总线 */
class EventBus {
    companion object {
        private var instance: EventBus? = null
        fun instance(): EventBus {
            if (instance == null) instance = EventBus()
            return instance!!
        }
    }
    private var _exceptHandler: ((String,Any,Throwable) -> Unit)? = null
    private val _subMap = mutableMapOf<String, ArrayList<(Any) -> Unit>>()
    private val _locker = Any()

    /** 订阅事件 */
    fun subscribe(topic: String, h: (Any) -> Unit) {
        synchronized(this._locker) {
            if (this._subMap.contains(topic)) {
                this._subMap[topic]!!.add(h)
            } else {
                this._subMap[topic] = arrayListOf(h)
            }
        }
    }

    /** 异常处理 */
    fun except(e: (String,Any,Throwable) -> Unit) {
        this._exceptHandler = e
    }

    /** 发布事件 */
    fun publish(topic: String, data: Any): Error? {
        val list = this._subMap[topic]
        if (list == null) {
            println(" [ eventbus]:no subscribes for topic $topic")
            return null
        }
        return catch {
            list.forEach { it.invoke(data) }
        }.except { this._exceptHandler?.invoke(topic,data,it) }.error()
    }
}