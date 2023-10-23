package net.fze.common

import net.fze.util.Systems
import org.junit.jupiter.api.Test


internal class KotlinLangExtensionTest {

    @Test
    fun coroutines() {
        Systems.std.coroutinesRun() {
            Thread.sleep(1000)
            print("hello world")
        }

        print("hello2")
        Thread.sleep(10000);
    }
}