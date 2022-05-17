package net.fze.util;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TimesTest {

    @Test
    void unixOfDate() {
       long v = Times.unixOfDate(new Date()); // 1652803200
//       long v2 = Times.unix() - Calendar.getInstance(). Times.unix() % (3600);
       System.out.println(Times.unixTime(v,0).toString());
       System.out.println(v);
    }

    @Test
    void unixOfDateOver() {
        long v = Times.unixOfDateOver(new Date());
        long v2 = Times.unix() - Times.unix() %1000;
        System.out.println(v+"/"+v2);
    }
}