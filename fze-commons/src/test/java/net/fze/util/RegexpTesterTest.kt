package net.fze.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RegexpTesterTest {

    @Test
    fun isNumber() {
        var b = RegexpTester.isNumber("1.3")
        Assertions.assertEquals(b,true)
         b = RegexpTester.isNumber("hh")
        Assertions.assertEquals(b,true)
    }
}