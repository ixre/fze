package net.fze.arch.commons.std.extend;

import net.fze.arch.commons.std.Typed;
import org.junit.jupiter.api.Test;

import java.util.Date;

class TimeExtensionsTest {

    /**
     * 测试获取月份的开始时间
     */
    @Test
    void unixOfMonth() {
        Date d = Typed.time.unixTime(1543679999, 0);
        int begin = Typed.time.unixOfMonth(d);
        int over = Typed.time.unixOfMonthOver(d);
        System.out.println(begin + "/" + over);
    }
}