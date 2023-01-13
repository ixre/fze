package net.fze.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogsTest {

    @Test
    void log() {
        Logs.log("--hello,jarry");
    }

    @Test
    void info() {
        Logs.info("--hello,jarry");
    }
}