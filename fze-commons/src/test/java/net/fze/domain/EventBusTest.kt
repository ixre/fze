package net.fze.domain

import org.junit.jupiter.api.Test

internal class EventBusTest {
    @Test
    fun testEventBus() {
        EventBus.instance().subscribe("1") {
            println("---${it}")
            Thread.sleep(1000)
        }
        EventBus.instance().subscribeAsync("1") {
            println("---sese ${it}")
            Thread.sleep(1000)
        }
        EventBus.instance().publish("1", "haha");

        Thread.sleep(10000)
    }
}