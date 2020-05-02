package com.github.ixre.fze.web.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private final HttpExchange ex;
    private Map<String, FileItem> files = new HashMap<>();
    private Map<String, String> urlValues = new HashMap<>();
    private Map<String, String> formValues = new HashMap<>();
    private boolean parsed = false;

    public HttpRequest(HttpExchange ex) {
        this.ex = ex;
    }

    /**
     * 获取请求方法
     */
    public String method() {
        return this.ex.getRequestMethod();
    }

    /**
     * 获取URI地址
     */
    public URI uri() {
        return this.ex.getRequestURI();
    }

    /**
     * 获取主机头
     */
    public String host() {
        return this.header().getFirst("Host");
    }

    /**
     * 获取请求主体
     */
    public InputStream body() {
        return this.ex.getRequestBody();
    }

    /**
     * 获取用户代理
     */
    public String userAgent() {
        return this.header().getFirst("User-Agent");
    }

    /**
     * 获取HTTP协议
     */
    public String getProtocol() {
        return this.ex.getProtocol();
    }

    /**
     * 获取连接协议
     */
    public String getScheme() {
        return "";
    }

    /**
     * 获取查询
     */
    public String getQuery() {
        return this.ex.getRequestURI().getQuery();
    }

    /**
     * 获取路径
     */
    public String getPath() {
        return this.ex.getRequestURI().getPath();
    }

    private void parse() {
        if (!this.parsed) {
            try {
                this.parseQuery(this.urlValues, this.uri().getQuery());
                this.parseMultipartForm();
                this.parseForm();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    private void parseForm() throws IOException {
        InputStreamReader isr = new InputStreamReader(this.ex.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(this.formValues, query);
    }

    private void parseQuery(Map<String, String> dst, String query)
            throws UnsupportedEncodingException {
        if (query == null || query.equals("")) return;
        String[] pairs = URLDecoder.decode(query, "UTF-8").split("&");
        for (String p : pairs) {
            int i = p.indexOf((int) '=');
            if (i != -1) {
                dst.put(p.substring(0, i), p.substring(i + 1));
            }
        }
    }

    private void parseMultipartForm() throws FileUploadException {
        String contentType = this.contentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            RequestContext request = FileUpload.createContext(this.ex);
            DiskFileItemFactory d = new DiskFileItemFactory();
            ServletFileUpload up = new ServletFileUpload(d);
            List<FileItem> list = up.parseRequest(request);
            for (FileItem f : list) {
                this.files.put(f.getFieldName(), f);
            }
        }
    }

    private String contentType() {
        return this.header().getFirst("Content-Type");
    }

    private Headers header() {
        return this.ex.getRequestHeaders();
    }

    public FileItem getFile(String name) {
        this.parse();
        if (this.files.containsKey(name)) {
            return this.files.get(name);
        }
        return null;
    }

    @NotNull
    String formValue(@NotNull String k) {
        this.parse();
        return this.formValues.getOrDefault(k, "");
    }

    @NotNull
    Map<String, String> forms() {
        this.parse();
        return this.formValues;
    }

    @NotNull
    Map<String, String> queryValues() {
        this.parse();
        return this.urlValues;
    }

    @NotNull
    String queryValue(@NotNull String k) {
        this.parse();
        return this.urlValues.getOrDefault(k, "");
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public String remoteAddr() {
        String[] keys = new String[]{
                "X-REAL-IP",  // nginx自定义配置
                "X-FORWARDER-FOR",
                "Proxy-Client-IP",
                "WL-Proxy-CLIENT-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };
        String ip = this.ex.getRemoteAddress().toString();
        for(int i=0;i<keys.length;i++){
            ip = this.header().getFirst(keys[i]);
            if (ip != null && !ip.equals("") && !"unknown".equalsIgnoreCase(ip)){
                break;
            }
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            return ip.substring(0, ip.indexOf(",")).trim();
        }
        return "";
    }

    public String referrer() {
        return this.header().getFirst("HTTP_REFERER");
    }
}
