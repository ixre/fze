package net.fze.domain

import org.junit.jupiter.api.Test

internal class EventBusTest {
    @Test
    fun testEventBus() {
        EventBus.getDefault().subscribe("1") {
            println("---${it}")
            Thread.sleep(1000)
        }
        EventBus.getDefault().subscribeAsync("1") {
            println("---sese ${it}")
            Thread.sleep(1000)
        }
        EventBus.getDefault().publish("1", "haha");

        Thread.sleep(10000)
    }
}