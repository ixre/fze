package com.github.ixre.fze.commons.std.extend;

import com.github.ixre.fze.commons.std.Typed;
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