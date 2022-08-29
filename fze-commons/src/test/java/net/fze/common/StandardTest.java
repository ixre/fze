package net.fze.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardTest {

    @Test
    void dev() {
        Standard.resolveEnvironment(StandardTest.class);
        System.out.println("haha");
    }
}