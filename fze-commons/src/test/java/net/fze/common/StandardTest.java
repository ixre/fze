package net.fze.common;

import net.fze.util.Systems;
import org.junit.jupiter.api.Test;

class StandardTest {

    @Test
    void dev() {
        Systems.resolveEnvironment(StandardTest.class);
        System.out.println("haha");
    }
}