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

/**
val data1 = Test1Event("message from event1")
val data2 = Test2Event("message from event2")

EventBus.getDefault().subscribe(Test1Event::class.java){
    println("--- ${it.name}")
}
EventBus.getDefault().subscribeAsync(Test2Event::class.java) {
    println("--- ${it.name}")
}
EventBus.getDefault().publish(data1);
EventBus.getDefault().publish(data2);

Thread.sleep(10000)
 */

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

    private class EventWrapper<T>(val async: Boolean, val handler: (T) -> Unit)

    private val dispatcher = EventDispatcher<EventWrapper<Any>>()
    private var _exceptHandler: ((String, Any, Throwable) -> Unit)? = null

    /** 订阅事件 */
    fun <T> subscribe(event: Class<T>, h: (T) -> Unit) {
        this.dispatcher.subscribe(event.name, EventWrapper(false) {
            h(it as T)
        })
    }

    /** 订阅异步事件 */
    fun <T> subscribeAsync(event: Class<T>, h: (T) -> Unit) {
        this.dispatcher.subscribe(event.name, EventWrapper(true){
            h(it as T)
        })
    }

    /** 异常处理 */
    fun except(e: (String, Any, Throwable) -> Unit) {
        this._exceptHandler = e
    }

    /** 发布事件 */
    fun <T> publish(data: T): Error? {
        val eventName = EventBusTypes.getName(data)
        val list = this.dispatcher.gets(eventName)
        if (list.size == 0) {
            println(" [ EventBus][ ${this.name}]: no subscribes for class $eventName, please check class is match?")
            return null
        }
        return catch {
            list.forEach {
                if (it.async) {
                    GlobalScope.launch { it.handler.invoke(data as Any) }
                } else {
                    it.handler.invoke(data as Any)
                }
            }
        }.except { this._exceptHandler?.invoke(eventName, data as Any, it) }.error()
    }
}
