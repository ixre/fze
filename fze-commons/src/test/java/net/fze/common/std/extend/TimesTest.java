package net.fze.common.std.extend;

import net.fze.util.Times;
import org.junit.jupiter.api.Test;

import java.util.Date;

class TimesTest {

    /**
     * 测试获取月份的开始时间
     */
    @Test
    void unixOfMonth() {
        Date d = Times.unixTime(1543679999, 0);
        int begin = Times.unixOfMonth(d);
        int over = Times.unixOfMonthOver(d);
        System.out.println(begin + "/" + over);
    }
}