package net.fze.common.http;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HttpClient 客户端
 */
public class HttpClient {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:120.0) Gecko/20100101 Firefox/120.0";

    /**
     * 设置Cookie管理器
     * @param cookieManager cookie管理器
     */
    public static void setCookieManager(CookieManager cookieManager) {
        if(CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(cookieManager == null ? new CookieManager(null, CookiePolicy.ACCEPT_ALL) : cookieManager);
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
    public static byte[] request(String url, String method, byte[] data, int timeout) {
        HttpRequest req = newRequest(url, method)
                .body(data).timeout(timeout).build();
        try {
            return HttpRequestUtils.doRequest(req,null);
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
            return HttpRequestUtils.doRequest(req,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] post(String url, byte[] data, int timeout) {
        HttpRequest req = newRequest(url, "POST")
                .body(data)
                .contentType(ContentType.FORM)
                .timeout(timeout).build();
        try {
            return HttpRequestUtils.doRequest(req,null);
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
            return HttpRequestUtils.doRequest(b.build(),null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起HTTP/HTTPS请求
     */
    public static byte[] request(HttpRequest r, Consumer<HttpURLConnection> consumer) {
        try {
            return HttpRequestUtils.doRequest(r,consumer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起HTTP/HTTPS请求
     * @param r 请求
     * @return 响应
     */
    public static byte[] request(HttpRequest r) {
        return request(r,null);
    }

}
