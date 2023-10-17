package net.fze.util

import org.junit.jupiter.api.Test


class StringUtilsTest {
    @Test
    fun testMd5() {
        println(Crypto.shortMd5("hello_world"))
        println(System.getProperty("java.version"))
    }
}
