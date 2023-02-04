package net.fze.util

import net.fze.jdk.jdk8.Lists
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ListUtilsTest {
    @Test
    fun of() {
        val list1 = Lists.of(1, 2)
        Assertions.assertEquals(list1.size, 2)
        val list2 = Lists.of<Int>()
        Assertions.assertEquals(list2.size, 0)

    }
}