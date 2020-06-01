package net.fze.web.http;


import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestTest {
    @Test
    public void testQueryValue() throws URISyntaxException, UnsupportedEncodingException {
        String url = "http://localhost:8080/pay/result?trade_no=2018761683237&err_code=1&err_msg=%25u5F53%25u524D%25u9875%25u9762%25u7684URL%25u672A%25u6CE8%25u518C%253Ahttps%253A//pay-gw.meizhuli.net/pay/wxpay/prepare";
        URI uri = new URI(url);
        System.out.println(uri.getQuery());
        Map<String, String> params = new HashMap<>();
        parseQuery(params, uri.getQuery());
        System.out.println(params.get("err_msg"));
    }

    private void parseQuery(Map<String, String> dst, String query)
            throws UnsupportedEncodingException {
        if (query == null || query.equals("")) return;
        String[] pairs = query.split("&");
        for (String p : pairs) {
            int i = p.indexOf((int) '=');
            if (i != -1) {
                dst.put(p.substring(0, i), URLDecoder.decode(p.substring(i + 1), "UTF-8"));
            }
        }
    }
}
