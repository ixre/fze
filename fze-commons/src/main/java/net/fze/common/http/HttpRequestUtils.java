package net.fze.common.http;

import net.fze.util.Types;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

public class HttpRequestUtils {

    /**
     * 发送https请求
     *
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static byte[] doRequest(HttpRequest req,Consumer<HttpURLConnection> consumer) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        String prefix = req.getUrl().substring(0, 5).toLowerCase();
        if ("https".equals(prefix)) {
            return httpsRequest(req,consumer);
        }
        return httpRequest(req);
    }

    /**
     * 发送https请求
     *
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    private static byte[] httpRequest(HttpRequest req) throws IOException {
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        HttpURLConnection conn = (HttpURLConnection) new URL(req.getUrl()).openConnection();
        applyRequestParams(conn, req);
        return getResponse(conn, req.getBody(),null);
    }

    private static void applyRequestParams(HttpURLConnection conn, HttpRequest req) throws IOException {
        prepareConnection(conn, req.getHeaders());
        setContentType(conn, req.getContentType());
        // 设置请求方式（GET/POST）
        conn.setRequestMethod(req.getMethod());
        if (req.getTimeout() > 0) {
            conn.setConnectTimeout(req.getTimeout());
        }
        if (req.getCookies() != null) {
            conn.setRequestProperty("Cookie", req.getCookies().toString());
        }
    }

    /**
     * 发送https请求
     *
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    private static byte[] httpsRequest(HttpRequest req,Consumer<HttpURLConnection> consumer) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(req.getUrl()).openConnection();
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = new TrustManager[]{new TrustAnyTrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        conn.setSSLSocketFactory(ssf);
        applyRequestParams(conn, req);
        return getResponse(conn, req.getBody(),consumer);
    }

    /**
     * 设置请求头
     */
    private static void setContentType(HttpURLConnection conn, String contentType) {
        if (contentType != null && !contentType.isEmpty()) {
            conn.setRequestProperty("Content-Type", contentType);
        }
    }

    /**
     * 添加HTTP头
     *
     * @param conn    连接
     * @param headers 头部
     */
    private static void prepareConnection(HttpURLConnection conn, Map<String, String> headers) throws IOException {
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获取响应
     *
     * @param conn 连接
     * @param data 数据
     * @return 字节数组
     */
    private static byte[] getResponse(HttpURLConnection conn, byte[] data, Consumer<HttpURLConnection> consumer ) throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            if (data != null && data.length > 0) {
                OutputStream os = conn.getOutputStream();
                os.write(data);
                os.flush();
                os.close();
            }

            InputStream is = conn.getInputStream();
            String encoding = conn.getHeaderField("content-encoding");
            if ("gzip".equalsIgnoreCase(encoding)) {
                // gzip解压缩
                is = new GZIPInputStream(is);
            }
            if (consumer != null) {
                consumer.accept(conn);
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                stream.write(buffer, 0, bytesRead);
            }
            is.close();
            return stream.toByteArray();
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
        StringBuilder buf = new StringBuilder();
        int i = 0;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ > 0) {
                    buf.append("&");
                }
                buf.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
            }
            return buf.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将查询转换为字典
     */
    public static Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return new HashMap<>();
        }
        try {
            String[] pairs = query.split("&");
            Map<String, String> mp = new HashMap<>();
            for (String p : pairs) {
                int i = p.indexOf('=');
                if (i != -1) {
                    mp.put(URLDecoder.decode(p.substring(0, i), StandardCharsets.UTF_8.name()),
                            URLDecoder.decode(p.substring(i + 1), StandardCharsets.UTF_8.name()));
                }
            }
            return mp;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] parseBody(Map<String, String> params, boolean json) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        if (json) {
            return Types.toJson(params).getBytes(StandardCharsets.UTF_8);
        }
        return toQuery(params).getBytes(StandardCharsets.UTF_8);
    }
}
