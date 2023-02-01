package net.fze.jdk;

import net.fze.jdk.jdk8.Maps;
import net.fze.util.Collections;
import net.fze.util.Types;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapsTest {

    @Test
    void excludes() {
    }

    @Test
    void picks() {
        Map<String,Object> maps = Maps.of("a",1,"b","user","c",true);
        System.out.println(Types.toJson(Collections.picks(maps,"a","c")));
        System.out.println(Types.toJson(Collections.excludes(maps,"a","c")));
    }
}
