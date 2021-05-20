package net.fze.common

import org.junit.jupiter.api.Test



internal class KotlinLangExtensionTest {

    @Test
     fun coroutines() {
        Standard.std.coroutinesRun() {
            Thread.sleep(1000)
            print("hello world")
        }

        print("hello2")
        Thread.sleep(10000);
    }
}