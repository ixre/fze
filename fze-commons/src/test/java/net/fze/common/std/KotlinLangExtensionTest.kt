package net.fze.common.std

import net.fze.util.Strings
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class KotlinLangExtensionTest {

    @Test
    fun randomLetters() {
        var map = mutableMapOf<String, String>()
        var str = ""
        for (i in 0 until 10000) {
            str = Strings.randomLetters(6)
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
        val s = Strings.replaceN("13162222872", 3, 4, "*")
        println(" content = $s")
    }
}