package net.fze.arch.commons.util

import org.junit.jupiter.api.Test


class StringUtilsTest {
    @Test
    fun testMd5() {
        println(StrUtils.md5_16("hello_world"))
    }
}
