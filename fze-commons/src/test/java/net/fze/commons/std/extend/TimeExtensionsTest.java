package net.fze.commons.std.extend;

import net.fze.util.Types;
import org.junit.jupiter.api.Test;

import java.util.Date;

class TimeExtensionsTest {

    /**
     * 测试获取月份的开始时间
     */
    @Test
    void unixOfMonth() {
        Date d = Types.time.unixTime(1543679999, 0);
        int begin = Types.time.unixOfMonth(d);
        int over = Types.time.unixOfMonthOver(d);
        System.out.println(begin + "/" + over);
    }
}