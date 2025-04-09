package net.fze.common.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String contentType = ContentType.NONE.getEncodeType();
    private int timeout = 0;
    private Map<String, String> headers = new HashMap<>();

    private byte[] body = new byte[]{};
    private String method = "GET";
    private String url = "";
    private HttpCookies _cookies;
    private String _proxyHost;
    private int _proxyPort;

    protected HttpRequest(String url,String method){
        this.url = url;
        this.method = method;
    }


    public String getUrl() {
        return url;
    }
    public String getMethod() {
        return method;
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

    public void setHttpProxy(String host, int port) {
        this._proxyHost = host;
        this._proxyPort = port;
    }

    public String getProxyHost() {
        return this._proxyHost;
    }
    public int getProxyPort() {
        return this._proxyPort;
    }
}
