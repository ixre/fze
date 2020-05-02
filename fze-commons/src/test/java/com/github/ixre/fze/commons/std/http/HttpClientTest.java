package com.github.ixre.fze.commons.std.http;


import org.junit.jupiter.api.Test;

public class HttpClientTest {
    @Test
    public void testRequest()
            throws Exception {
        String url = "https://mzl-api.meizhuli.net/prod/mzl/v1.2";
        byte[] data =
                "api=payment.getPaymentOrderState&key=a80line365mzl141901&product=mzl&product_kind=alpha&sign=f4e3200e06f5825635b469168f12644913d579bf&sign_type=sha1&storeCode=62321d6f74b4400fabc89ba4d44ac13c&tradeNo=MS180615675659&version=1.2.7.01"
                        .getBytes();
        byte[] ret = HttpClient.request(url, "POST", data, 10000);
        System.out.println(new String(ret));
    }
}
