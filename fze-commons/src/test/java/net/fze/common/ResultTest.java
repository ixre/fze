package net.fze.common;

import net.fze.jdk.jdk8.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void asMap() {
        Result r = Result.success(Maps.of("message","hello world"));
        Map<String, Object> map = r.asMap();
        System.out.println(map.get("message"));
    }
}