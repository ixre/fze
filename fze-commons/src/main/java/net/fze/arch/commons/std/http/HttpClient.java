package net.fze.arch.commons.std.http;

import net.fze.arch.commons.std.Types;
import net.fze.arch.commons.util.OsUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
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
    public static byte[] request(String url, String method, byte[] data, int timeout)
            throws Exception {
        String prefix = url.substring(0, 5).toLowerCase();
        if (prefix.equals("https")) {
            return httpsRequest(url, method, data, null, timeout);
        }
        return httpRequest(url, method, data, null, timeout);
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
    public static byte[] request(String url, String method, byte[] data, Map<String, String> headers, int timeout)
            throws Exception {
        String prefix = url.substring(0, 5).toLowerCase();
        if (prefix.equals("https")) {
            return httpsRequest(url, method, data, headers, timeout);
        }
        return httpRequest(url, method, data, headers, timeout);
    }

    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param method     请求方式（GET、POST）
     * @param data       提交的数据
     * @param headers    头部
     * @param timeout    超时时间
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    private static byte[] httpRequest(String requestUrl, String method, byte[] data, Map<String, String> headers, int timeout)
            throws Exception {
        // 从上述SSLContext对象中得到SSLSocketFactory对象

        HttpURLConnection conn = (HttpURLConnection) new URL(requestUrl).openConnection();
        prepareConnection(conn, headers);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        if (timeout > 0) {
            conn.setConnectTimeout(timeout);
        }
        // 设置请求方式（GET/POST）
        conn.setRequestMethod(method);
        if (timeout > 0) {
            conn.setConnectTimeout(timeout);
        }
        return getResponse(conn, data);
    }

    /**
     * 添加HTTP头
     *
     * @param conn    连接
     * @param headers 头部
     * @throws Exception
     */
    private static void prepareConnection(HttpURLConnection conn, Map<String, String> headers) throws Exception {
        if (headers == null) return;
        for (Map.Entry<String, String> p : headers.entrySet()) {
            if (Types.emptyOrNull(p.getKey()) || Types.emptyOrNull(p.getValue())) {
                throw new Exception("Headers contain null key or null value");
            }
            conn.setRequestProperty(p.getKey(), p.getValue());
        }
    }


    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param method     请求方式（GET、POST）
     * @param data       提交的数据
     * @param headers    头部
     * @param timeout    超时时间
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    private static byte[] httpsRequest(String requestUrl, String method, byte[] data, Map<String, String> headers, int timeout)
            throws Exception {
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {new TrustAnyTrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        HttpsURLConnection conn = (HttpsURLConnection) new URL(requestUrl).openConnection();
        prepareConnection(conn, headers);
        conn.setSSLSocketFactory(ssf);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        conn.setRequestMethod(method);
        if (timeout > 0) {
            conn.setConnectTimeout(timeout);
        }
        return getResponse(conn, data);
    }


    private static byte[] getResponse(HttpURLConnection conn, byte[] data) throws IOException {
        try {
            // 写入数据
            if (data != null && data.length != 0) {
                OutputStream ost = conn.getOutputStream();
                ost.write(data);
                ost.flush();
                ost.close();
            }
      /*
      // 判断响应状态
      if (conn.getResponseCode() != 200) {
        throw new IOException(
            String.format(
                "远程服务器响应: Http %d:%s",
                conn.getResponseCode(), String.valueOf(conn.getResponseMessage())));
      }
      */
            // 从输入流读取返回内容
            InputStream ist = conn.getInputStream();
            byte[] ret = OsUtils.streamToByteArray(ist);
            ist.close();
            return ret;
        } catch (Exception ex) {
            throw ex;
        } finally {
            conn.disconnect();
        }
    }

    /**
     * 将Map转换为查询
     *
     * @param params 参数
     * @return 查询
     */
    public static String toQuery(Map<String, String> params) {
        int i = 0;
        String v;
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (i++ > 0) {
                buf.append("&");
            }
            try {
                v = URLEncoder.encode(e.getValue(), "utf-8");
                buf.append(e.getKey()).append("=").append(v);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return buf.toString();
    }

    /**
     * 将查询转换为字典
     */
    public static Map<String, String> parseQuery(String query) {
        if (query != null && !query.equals("")) {
            try {
                String[] pairs = URLDecoder.decode(query, "UTF-8").split("&");
                Map<String, String> mp = new HashMap<>();
                for (String p : pairs) {
                    int i = p.indexOf((int) '=');
                    if (i != -1) {
                        mp.put(p.substring(0, i), p.substring(i + 1));
                    }
                }
                return mp;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return new HashMap<>();
    }
}
