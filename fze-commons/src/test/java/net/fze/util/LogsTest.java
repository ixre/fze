package net.fze.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogsTest {

    @Test
    void log() {
        new Logs().log("--hello,jarry");
    }

    @Test
    void info() {
        new Logs().info("--hello,jarry");
    }
}