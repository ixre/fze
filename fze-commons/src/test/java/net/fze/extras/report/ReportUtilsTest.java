package net.fze.extras.report;

import net.fze.util.Types;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportUtilsTest {

    @Test
    void timeRangeSQL() {
        String create_time = ReportUtils.timeRangeSQL("[]", "create_time");
        System.out.println(create_time);
        String create_time2 = ReportUtils.timeRangeSQL("[2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]", "create_time");
        System.out.println(create_time2);

    }

    @Test
    void testTimeRangeSQL() {
    }

    @Test
    void timeRangeSQLByJSONTime() {
    }

    @Test
    void parseParams() {
        String query= "%7B%22keyword%22%3A%22%22%2C%22where%22%3A%220%3D0%22%2C%22declare_org%22%3A%22%22%2C%22order_no%22%3A%22%22%2C%22status%22%3A0%2C%22time_range%22%3A%5B%5D%2C%22order_by%22%3A%22id%20DESC%22%7D";
        Params params = ReportUtils.parseParams(query);
        System.out.println(Types.toJson(params.getValue()));
    }
}