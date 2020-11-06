package net.fze.common.http;

import net.fze.util.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * HttpClient 客户端
 */
public class HttpClient {
    /**
     * 发起HTTP/HTTPS请求
     *
     * @param url     url地址
     * @param method  请求方法,GET/POST
     * @param data    参数
     * @param timeout 超时时间，如果为0，则不限超时时间
     * @return 响应结果
     */
    public static byte[] request(String url, String method, byte[] data, int timeout){
        HttpRequest req = new HttpRequestBuilder().create(url, method)
                .body(data).timeout(timeout).build();
        return HttpUtilsKt.doRequest(req);
    }

    public static byte[] get(String url, int timeout) {
        HttpRequest req = new HttpRequestBuilder().create(url, "GET")
                .timeout(timeout).build();
        return HttpUtilsKt.doRequest(req);
    }

    public static byte[] post(String url,  byte[] data, int timeout) {
        HttpRequest req = new HttpRequestBuilder().create(url, "POST")
                .body(data).timeout(timeout).build();
        return HttpUtilsKt.doRequest(req);
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
        HttpRequestBuilder b = new HttpRequestBuilder().create(url, method)
                .headers(headers).body(data).timeout(timeout);
        return HttpUtilsKt.doRequest(b.build());
    }

    /**
     * 发起HTTP/HTTPS请求
     */
    public static byte[] request(HttpRequest r) {
        return HttpUtilsKt.doRequest(r);
    }

    /**
     * 将参数转为二进制数组
     */
    public static byte[] parseBody(@Nullable Map<String, String> params) {
        return HttpUtilsKt.parseBody(params, false);
    }

    /**
     * 将参数转为JSON二进制数组
     * @param params
     * @return
     */
    public static byte[] parseJsonBody(@NotNull Object params) {
        if(params== null)return null;
        return Types.toJson(params).getBytes();
    }
}
