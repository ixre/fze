package net.fze.arch.web.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {
    private final HttpExchange ex;

    public HttpResponse(HttpExchange ex) {
        this.ex = ex;
    }

    Error write(byte[] b) {
        try {
            this.rsp200(b.length);
            this.ex.getResponseBody().write(b);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return new Error(ex.getMessage());
        }
        return null;
    }

    private void rsp200(int length) throws IOException {
        this.header().set("Content-Type", "text/html;charset=UTF-8");
        this.ex.sendResponseHeaders(200, length);
    }

    private void rspCode(int code) throws IOException {
        this.ex.sendResponseHeaders(code, 0);
    }

    public Headers header() {
        return this.ex.getResponseHeaders();
    }

    /**
     * 输出错误
     *
     * @param err 错误
     */
    public void errorOutput(Error err) {
        try {
            this.header().set("Content-Type", "text/html;charset=UTF-8");
            this.write(("HTTP 500 :" + err.getMessage()).getBytes());
            this.rspCode(500);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 301 或302跳转
     *
     * @param code   http code
     * @param target 目标地址
     */
    Error redirect(int code, String target) {
        try {
            if (code != 301 && code != 302) {
                throw new Exception("status code error");
            }
            this.header().set("Location", target);
            this.rspCode(code);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return new Error(ex.getMessage());
        }
        return null;
    }

    public OutputStream getWriter() {
        return this.ex.getResponseBody();
    }

    public void status(int code) throws IOException {
        this.ex.sendResponseHeaders(code, 0);
    }
}
