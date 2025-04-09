package net.fze.common.http;




import net.fze.util.Assert;

import java.util.Map;

public class HttpRequestBuilder {

    private final HttpRequest req;


    private HttpRequestBuilder(String url, String method) {
      this.req = new HttpRequest(url,method);
    }

    public static HttpRequestBuilder create(String url, String method) {
        return new HttpRequestBuilder(url, method);
    }

    public HttpRequestBuilder headers(Map<String, String> headers) {
        if (headers != null) {
            this.req.getHeaders().putAll(headers);
        }
        return this;
    }

    public HttpRequestBuilder setHeader(String key, String value) {
        this.req.getHeaders().put(key, value);
        return this;
    }

    public HttpRequestBuilder timeout(int second) {
        this.req.setTimeout(second);
        return this;
    }

    public HttpRequestBuilder contentType(ContentType contentType) {
        if (contentType != ContentType.NONE) {
            this.req.setContentType(contentType.getEncodeType());
        }
        return this;
    }

    public HttpRequestBuilder userAgent(String userAgent) {
        Assert.isNullOrEmpty(userAgent, "userAgent can not be null");
        this.req.getHeaders().put("User-Agent", userAgent);
        return this;
    }

    public HttpRequestBuilder body(byte[] bytes) {
        Assert.isNullOrEmpty(bytes, "body can not be null");
        this.req.setBody(bytes);
        return this;
    }

    public HttpRequestBuilder setCookies(HttpCookies cookies) {
        this.req.setCookies(cookies);
        return this;
    }


    public HttpRequest build() {
        if (this.req.getUrl().isEmpty()) throw new IllegalArgumentException("url");
        return this.req;
    }

    public HttpRequest setProxy(String host, int port) {
        this.req.setHttpProxy(host,port);
        return this.req;
    }
}
