package net.fze.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fze.common.catch

class EventDispatcher<T> {
    private val _subMap = mutableMapOf<String, ArrayList<T>>()
    private val _locker = Any()

    /** 订阅 */
    fun subscribe(key: String, h: T): Int {
        synchronized(this._locker) {
            if (this._subMap.contains(key)) {
                this._subMap[key]!!.add(h)
            } else {
                this._subMap[key] = arrayListOf(h)
            }
            return this._subMap[key]?.size ?: 1 - 1;
        }
    }

    fun gets(topic: String): ArrayList<T> {
        return this._subMap[topic] ?: arrayListOf()
    }
}


/** 事件总线 */
class EventBus(val name: String = "") {
    companion object {
        private var defaultInstance: EventBus? = null

        @JvmStatic
        fun getDefault(): EventBus {
            if (defaultInstance == null) defaultInstance = EventBus("default")
            return defaultInstance!!
        }
    }

    private class EventWrapper(val async: Boolean, val handler: (Any) -> Unit)

    private val dispatcher = EventDispatcher<EventWrapper>()
    private var _exceptHandler: ((String, Any, Throwable) -> Unit)? = null

    /** 订阅事件 */
    fun subscribe(topic: String, h: (Any) -> Unit) {
        this.dispatcher.subscribe(topic, EventWrapper(false, h))
    }

    /** 订阅异步事件 */
    fun subscribeAsync(topic: String, h: (Any) -> Unit) {
        this.dispatcher.subscribe(topic, EventWrapper(true, h))
    }

    /** 异常处理 */
    fun except(e: (String, Any, Throwable) -> Unit) {
        this._exceptHandler = e
    }

    /** 发布事件 */
    fun publish(topic: String, data: Any): Error? {
        return this.postEvent(topic, data, false)
    }


    /** 发布事件 */
    private fun postEvent(topic: String, data: Any, async: Boolean): Error? {
        val list = this.dispatcher.gets(topic)
        if (list.size == 0) {
            println(" [ EventBus][ ${this.name}]: no subscribes for topic $topic")
            return null
        }
        return catch {
            list.forEach {
                if (it.async) {
                    GlobalScope.launch { it.handler.invoke(data) }
                } else {
                    it.handler.invoke(data)
                }
            }
        }.except { this._exceptHandler?.invoke(topic, data, it) }.error()
    }
}