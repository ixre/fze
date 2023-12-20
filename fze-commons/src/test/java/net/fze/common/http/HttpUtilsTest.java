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
        String s = HttpUtil.join("http://fze.net", "info.html", "size=100");
        Assertions.assertEquals(s, "http://fze.net/info.html?size=100");
        String s2 = HttpUtil.join("http://fze.net", "info.html?r=1", "size=100");
        Assertions.assertEquals(s2, "http://fze.net/info.html?r=1&size=100");
        String s3 = HttpUtil.join("http://fze.net/info.html?r=1", "", "size=100");
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
    void testGetBaseUrl() {
        String url = "http://baidu.com/fsdfsf";
        String ret = HttpUtil.getBaseURL(url, null);
        System.out.println("值为:" + ret);

    }

    /**
     * 测试带cookie请求
     */
    @Test
    void testRequestWithCookie() {

        String url = "https://hwpartner.meituan.com/apigw/outer/open/copp/device/view/app?sn=S4PAC21233100073&appName=";
        HttpCookies cookies = new HttpCookies();

        cookies.put("lt", "AgHPHwPLK_KDYX5Imw18WZRw8lBpCHVBkIhVehvhSIr3P-EuFl_jtqcdsbx4D96uO2DsmnhPVhBGQQAAAAC6HAAAMPMC7XrFPJJt1QMkohMbybMMK6SgMbHHLE9LWbsIj1Z2BqPipddqS94fm5EAKwf6");
        cookies.put("n", "QWv232262186");
        cookies.put("u", "3828315534");
        cookies.put("uuid", "1b16b6c8a60a4e359be6.1700902228.1.0.0");
        cookies.put("WEBDFPID", "1702956495177KGUAECC2960edaad10e294fa6f28397fe2285903589-1702956495177-1702956495177KGUAECC2960edaad10e294fa6f28397fe2285903589");

        HttpRequest req = HttpRequestBuilder.create(url, "GET")
                .setHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:120.0) Gecko/20100101 Firefox/120.0")
                .setCookies(cookies)
                .build();
        String ret = new String(HttpClient.request(req));
        System.out.printf(ret);
    }
}

