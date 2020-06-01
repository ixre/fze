package net.fze.commons.std.http;

import net.fze.commons.std.Types;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static boolean _httpsProxied = false;

    public static void httpsProxied(){
        _httpsProxied = true;
    }

    /**
     * 是否为HTTPS连接
     */
    public static boolean isTls(String url) {
        return url.toLowerCase().startsWith("https://");
    }

    /**
     * 拼接URL地址
     */
    public static String urlJoin(String host, String path, String query, boolean tls) {
        StringBuilder b = new StringBuilder();
        b.append(tls ? "https" : "http").append("://");
        b.append(host);
        if (!Types.emptyOrNull(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            b.append(path);
        }
        if (!Types.emptyOrNull(query)) {
            b.append("?").append(query);
        }
        return b.toString();
    }

    /* 获取当前请求的BaseURL */
    public static String getBaseURL(HttpServletRequest req) {
        String path = req.getRequestURI();
        String s = req.getRequestURL().toString();
        if (_httpsProxied) s = s.replace("http://", "https://");
        return s.substring(0, s.lastIndexOf(path));
    }

    /**
     * 获取请求的路径
     */
    public static String getRequestPath(HttpServletRequest req) {
        return req.getRequestURI();
    }

    /**
     * 将请求参数序列化为实体
     */
    public static <T> T mapEntity(HttpServletRequest req, Class<T> classes) {
        Map<String, String> data = new HashMap<>();
        Map<String, String[]> src = req.getParameterMap();
        for (String k : src.keySet()) {
            data.put(k, src.get(k)[0]);
        }
        return Types.mapObject(data, classes);
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String remoteAddr(HttpServletRequest request) {
        String[] keys = new String[]{
                "X-REAL-IP",  // nginx自定义配置
                "X-FORWARDER-FOR",
                "Proxy-Client-IP",
                "WL-Proxy-CLIENT-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };
        String ip = null;
        for (int i = 0; i < keys.length; i++) {
            ip = request.getHeader(keys[i]);
            if (ip != null && !ip.equals("") && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }
        if (ip == null) ip = request.getRemoteAddr();
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            return ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }
}
