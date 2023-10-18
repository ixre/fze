package net.fze.common.http;

import net.fze.util.Strings;
import net.fze.util.Types;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    /**
     * 是否为HTTPS连接
     */
    public static boolean isTls(String url) {
        return url.toLowerCase().startsWith("https://");
    }

    /**
     * 拼接URL地址
     */
    public static String join(String base, String path, String query) {
        StringBuilder b = new StringBuilder();
        b.append(base);
        if (!Strings.isNullOrEmpty(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            b.append(path);
        }
        if (!Strings.isNullOrEmpty(query)) {
            if (!query.startsWith("?")) {
                if (!Strings.isNullOrEmpty(base) && base.contains("?")) {
                    b.append("&");
                } else if (!Strings.isNullOrEmpty(path) && path.contains("?")) {
                    b.append("&");
                } else {
                    b.append("?");
                }
            }
            b.append(query);
        }
        return b.toString();
    }

    /* 获取当前请求的BaseURL */
    @Deprecated
    public static String getServletBaseURL(HttpServletRequest req) {
        String path = req.getRequestURI();
        String s = req.getRequestURL().toString();
        if (IsHttpsProxyRequest(k -> req.getHeader(k))) {
            s = s.replace("http://", "https://");
        }
        return s.substring(0, s.lastIndexOf(path));
    }

    /**
     * 获取当前请求的BaseURL,该方法自动检测https
     *
     * @param url URL
     * @param h   h
     * @return 返回域名前缀
     */
    public static String getBaseURL(String url, IHeaderValue h) {
        Pattern pattern = Pattern.compile("^([^/]+//[^/]+)/*");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return "";
        }
        String prefix = matcher.group(1);
        if (h != null && IsHttpsProxyRequest(k -> h.get(k))) {
            return prefix.replace("http://", "https://");
        }
        return prefix;
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
        return Types.fromJson(Types.toJson(data), classes);
        // return Types.mapObject(data, classes);
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String remoteAddr(HttpServletRequest request) {
        String ip = getRealIP(it -> request.getHeader(it));
        if (ip == null)
            ip = request.getRemoteAddr();
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            return ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }

    /**
     * 获取客户端真实的IP地址,如果返回空,则需要调用request的RemoteAddr方法以获取IP
     *
     * @return
     */
    public static String getRealIP(IHeaderValue headers) {
        String[] keys = new String[]{
                "X-REAL-IP", // nginx自定义配置
                "X-FORWARDER-FOR",
                "Proxy-Client-IP",
                "WL-Proxy-CLIENT-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };
        String ip = null;
        for (int i = 0; i < keys.length; i++) {
            ip = headers.get(keys[i]);
            if (ip != null && !ip.equals("") && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }
        return ip;
    }

    /// <summary>
    /// 是否通过代理https请求
    /// </summary>
    /// <param name="request"></param>
    /// <returns></returns>
    public static boolean IsHttpsProxyRequest(IHeaderValue h) {
        // nginx反向代理
        if (equalHeader(h, "X-Forwarded-Proto", "https"))
            return true;
        // 兼容西部数码虚拟主机
        return equalHeader(h, "SSL-FLAG", "SSL")
                || equalHeader(h, "From-Https", "on");
    }

    private static boolean equalHeader(IHeaderValue h, String key, String value) {
        String v = h.get(key);
        if (v != null) {
            return v.equals(value);
        }
        return false;
    }

    /**
     * 将查询转换为字典
     */
    public static Map<String, String> parseQuery(String query) {
        return HttpUtilsKt.parseQuery(query);
    }

    /**
     * 将Map转换为查询
     *
     * @param params 参数
     * @return 查询
     */
    public static String toQuery(Map<String, String> params) {
        return HttpUtilsKt.toQuery(params);
    }

    /**
     * 解码url
     *
     * @param url 地址
     * @return
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return url;
        }
    }

    /**
     * 编码url 地址
     */
    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return url;
        }
    }
}
