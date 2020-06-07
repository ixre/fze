package net.fze.commons.std.api.wrapper;

import net.fze.commons.http.HttpUtils;
import net.fze.commons.std.api.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet请求
 */
public class ServletRequest implements Request {
    private final String method;
    private HttpServletRequest request;

    public ServletRequest(HttpServletRequest r) {
        this.request = r;
        this.method = r.getMethod().toUpperCase();
    }

    private Map<String, String> parseForm(Map<String, String[]> data) {
        Map<String, String> params = new HashMap<>();
        for (String k : data.keySet()) {
            params.put(k, data.get(k)[0]);
        }
        return params;
    }

    @Override
    public Object rawRequest() {
        return this.request;
    }

    @Override
    public String getParameter(String key) {
        return this.request.getParameter(key);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return this.parseForm(this.request.getParameterMap());
    }

    @Override
    public String getRemoteAddr() {
        return HttpUtils.remoteAddr(this.request);
    }

    @Override
    public String getHeader(String key) {
        return this.request.getHeader(key);
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
