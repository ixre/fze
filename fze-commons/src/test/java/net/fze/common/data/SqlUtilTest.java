package net.fze.common.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlUtilTest {

    @Test
    void checkInject() {
        String query="action=1%20OR%20DELETE%20FROM%20member&s";
        if(!SqlUtil.checkInject(query)){
            Assertions.fail("query contains inject chars");
        }
    }
}