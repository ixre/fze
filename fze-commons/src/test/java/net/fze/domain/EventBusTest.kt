package net.fze.domain

import org.junit.jupiter.api.Test

internal class EventBusTest {
    class Test1Event(var name: String)
    class Test2Event(var name: String)

    @Test
    fun testEventBus() {
        val data1 = Test1Event("message from event1")
        val data2 = Test2Event("message from event2")
        EventBus.getDefault().except { e1, e2, e3 ->
        }
        EventBus.getDefault().subscribe(Test1Event::class.java) {
            println("--- ${it.name}")
            Thread.sleep(1000)
        }
        EventBus.getDefault().subscribeAsync(Test2Event::class.java) {
            println("--- ${it.name}")
            Thread.sleep(1000)
        }
        EventBus.getDefault().publish(data1);
        EventBus.getDefault().publish(data2);

        Thread.sleep(10000)
    }
}
