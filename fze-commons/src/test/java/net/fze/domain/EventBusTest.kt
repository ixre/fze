package net.fze.domain

import org.junit.jupiter.api.Test

internal class EventBusTest:EventBus.Handler<EventBusTest.Test1Event>{
    class Test1Event(val i:Int,var name: String)
    class Test2Event(var name: String)

    @Test
    fun testEventBus() {
        val data1 = Test1Event(0,"message from event1")
        val data2 = Test2Event("message from event2")
        EventBus.getDefault().except { e1, e2, e3 ->
        }
        EventBus.getDefault().subscribe(Test1Event::class.java,this);
        EventBus.getDefault().asyncSubscribe(Test2Event::class.java) {
            println("--- ${it.name}")
        }
        EventBus.getDefault().publish(data2)
        for(i in 1 until 100){
            EventBus.getDefault().publish(Test1Event(i, "message from event$i"))
        }

        Thread.sleep(10000)
    }
    override fun handle(t: Test1Event) {
        println("--- 通过接口方法输出:${t.name}")
        Thread.sleep(1000)
    }
}
