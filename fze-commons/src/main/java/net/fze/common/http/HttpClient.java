package net.fze.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient 客户端
 */
public class HttpClient {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:120.0) Gecko/20100101 Firefox/120.0";

    /**
     * 发起HTTP/HTTPS请求
     *
     * @param url     url地址
     * @param method  请求方法,GET/POST
     * @param data    参数
     * @param timeout 超时时间，如果为0，则不限超时时间
     * @return 响应结果
     */
    public static byte[] request(String url, String method, byte[] data, int timeout) {
        HttpRequest req = newRequest(url, method)
                .body(data).timeout(timeout).build();
        try {
            return HttpRequestUtils.doRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequestBuilder newRequest(String url, String method) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", USER_AGENT);
        return HttpRequestBuilder.create(url, method).headers(header);
    }

    public static byte[] get(String url, int timeout) {
        HttpRequest req = newRequest(url, "GET")
                .timeout(timeout).build();
        try {
            return HttpRequestUtils.doRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] post(String url, byte[] data, int timeout) {
        HttpRequest req = newRequest(url, "POST")
                .body(data).timeout(timeout).build();
        try {
            return HttpRequestUtils.doRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起HTTP/HTTPS请求
     *
     * @param url     url地址
     * @param method  请求方法,GET/POST
     * @param data    参数
     * @param timeout 超时时间，如果为0，则不限超时时间
     * @return 响应结果
     */
    public static byte[] request(String url, String method, byte[] data, Map<String, String> headers, int timeout) {
        HttpRequestBuilder b = newRequest(url, method)
                .headers(headers).body(data).timeout(timeout);
        try {
            return HttpRequestUtils.doRequest(b.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起HTTP/HTTPS请求
     */
    public static byte[] request(HttpRequest r) {
        try {
            return HttpRequestUtils.doRequest(r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
