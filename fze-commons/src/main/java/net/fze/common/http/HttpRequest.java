package net.fze.common.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String contentType = ContentTypes.NOT.getValue();
    private int timeout = 0;
    private Map<String, String> headers = new HashMap<>();

    private byte[] body = new byte[]{};
    private String method = "GET";
    private String url = "";
    private HttpCookies _cookies;

    public String getMethod() {
        return method;
    }

    void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setCookies(HttpCookies cookies) {
        this._cookies = cookies;
    }

    public HttpCookies getCookies() {
        return this._cookies;
    }
}
