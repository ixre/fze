package net.fze.ext.report;

import net.fze.util.Types;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;

class ReportUtilsTest {

    @Test
    void timeRangeSQL() {
        String create_time = ReportUtil.timestampSQLByJSONTime("[]", "create_time");
        System.out.println(create_time);
        String create_time2 = ReportUtil.timeSQLByJSONTime("[2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]", "create_time");
        System.out.println(create_time2);
        String create_time3 = ReportUtil.timestampSQLByJSONTime("[1661335074674,1661939874674]", "create_time");
        System.out.println(create_time3);



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
        Params params = ReportUtil.parseParams(query);
        System.out.println(Types.toJson(params.getValue()));
    }

    @Test
    void testParseTimeParses(){
        String query = "%7B%22keyword%22%3A%22%22%2C%22where%22%3A%220%3D0%22%2C%22cus_code%22%3A%22%22%2C%22sales_code%22%3A%22%22%2C%22time_range%22%3A%5B1661388569294%2C1661993369294%5D%2C%22state%22%3A-1%2C%22order_by%22%3A%22id%20DESC%22%7D";
        Params params = ReportUtil.parseParams(query);
        System.out.println(Types.toJson(params.getValue()));
        
        String decode = URLDecoder.decode(query);
        System.out.println("query="+decode);
        String create_time2 = ReportUtil.timeSQLByJSONTime(params.get("time_range"), "create_time");
        System.out.println(create_time2);
    }


    /**
     * 测试配置读取
     */
    @Test
    void testLoadConfigItem(){
        ItemConfig cfg = ReportUtil.readItemConfigFromXml("classpath:/query/test.xml");
        System.out.println(Types.toJson(cfg));
    }

    /**
     * 测试配置项管理器
     */
    @Test
    void testItemManager(){
        ItemManager manager = new ItemManager(null,"classpath:query",true);
        IReportPortal cfg = manager.getItem("test.xml");
        System.out.println(Types.toJson(cfg));
    }
}