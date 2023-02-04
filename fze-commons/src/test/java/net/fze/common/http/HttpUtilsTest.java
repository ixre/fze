package net.fze.common.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class HttpUtilsTest {
    @Test
    void getBaseURL() {
        // HttpServletRequest req = new ServletContext()
        // HttpUtils.getBaseURL()
    }

    @Test
    void join() {
        String s = HttpUtils.join("http://fze.net", "info.html", "size=100");
        Assertions.assertEquals(s, "http://fze.net/info.html?size=100");
        String s2 = HttpUtils.join("http://fze.net", "info.html?r=1", "size=100");
        Assertions.assertEquals(s2, "http://fze.net/info.html?r=1&size=100");
        String s3 = HttpUtils.join("http://fze.net/info.html?r=1", "", "size=100");
        Assertions.assertEquals(s3, "http://fze.net/info.html?r=1&size=100");
    }

    @Test
    void kjPay() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user_id", 282);
        data.put("pay_amt", 100);
        data.put("goods_name", "瓷砖-爱琴海灰");
        data.put("goods_num", 1);
        byte[] body = HttpClient.parseJsonBody(data);
        HttpRequest req = HttpRequestBuilder
                .create("http://localhost:8086/platform-api/zhPay/kjPay", "POST")
                .contentType(ContentTypes.JSON.getValue())
                .body(body)
                .timeout(30000)
                .build();
        byte[] ret = HttpClient.request(req);
        String s = new String(ret);
        System.out.print(s);
    }

    @Test
    void testGetBaseUrl(){
        String url ="http://baidu.com/fsdfsf";
        String ret = HttpUtils.getBaseURL(url,null);
        System.out.println("值为:"+ret);

    }
}

