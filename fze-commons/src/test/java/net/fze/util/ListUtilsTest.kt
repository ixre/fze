package net.fze.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ListUtilsTest {
    @Test
    fun of() {
        val list1 = net.fze.jdk.Lists.of(1, 2)
        Assertions.assertEquals(list1.size, 2)
        val list2 = net.fze.jdk.Lists.of<Int>()
        Assertions.assertEquals(list2.size, 0)

    }
}