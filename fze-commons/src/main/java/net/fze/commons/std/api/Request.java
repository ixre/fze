package net.fze.commons.std.api;

import java.util.Map;

public interface Request {
    // 获取原始请求对象
    Object rawRequest();

    String getParameter(String key);

    Map<String, String> getParameterMap();

    /**
     * 获取远程用户的IP地址
     */
    String getRemoteAddr();

    String getHeader(String key);

    String getMethod();
}