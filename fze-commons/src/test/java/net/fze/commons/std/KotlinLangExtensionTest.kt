package net.fze.commons.std

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class KotlinLangExtensionTest {

    @Test
    fun randomLetters() {
        var map = mutableMapOf<String, String>()
        var str = ""
        for (i in 0 until 10000) {
            str = Standard.std.randomLetters(6)
            println("$i|$str")
            if (map.containsKey(str)) {
                fail<String>("重复")
            } else {
                map[str] = ""
            }
        }
    }

    @Test
    fun replaceN() {
        val s = Standard.std.replaceN("13162222872", 3, 4, "*")
        println(" content = $s")
    }
}