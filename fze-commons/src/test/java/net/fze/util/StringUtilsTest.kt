package net.fze.util

import net.fze.util.crypto.CryptoUtils
import org.junit.jupiter.api.Test


class StringUtilsTest {
    @Test
    fun testMd5() {
        println(CryptoUtils.shortMd5("hello_world"))
        println(System.getProperty("java.version"))
    }
}
