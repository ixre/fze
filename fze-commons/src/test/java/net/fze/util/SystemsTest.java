package net.fze.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemsTest {

    @Test
    void isTestEnvironment() {
       System.out.println( Systems.isTestEnvironment());
    }

    @Test
    void getVersionCode() {
        String version1 = "1.0.0";
        System.out.println(Systems.getVersionCode(version1));

        String version2 = "1.01.123";
        System.out.println(Systems.getVersionCode(version2));
    }
}