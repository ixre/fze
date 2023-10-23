package net.fze.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemsTest {

    @Test
    void isTestEnvironment() {
       System.out.println( Systems.isTestEnvironment());
    }
}