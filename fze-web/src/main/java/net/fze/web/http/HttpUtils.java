package net.fze.web.http;

import net.fze.util.Strings;
import net.fze.util.Types;

public class HttpUtils {
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
        StringBuffer b = new StringBuffer();
        b.append(tls ? "https" : "http").append("://");
        b.append(host);
        if (!Strings.isNullOrEmpty(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            b.append(path);
        }
        if (!Strings.isNullOrEmpty(query)) {
            b.append("?").append(query);
        }
        return b.toString();
    }

    /**
     * 使用自定义路径和查询拼接URL地址
     */
    public static String urlJoinPath(HttpContext ctx, String path, String query, Boolean tls) {
        return urlJoin(ctx.request().host(), path, query, tls);
    }

    public static String baseUrl(HttpContext ctx, Boolean tls) {
        return urlJoin(ctx.request().host(), null, null, tls);
    }
}
