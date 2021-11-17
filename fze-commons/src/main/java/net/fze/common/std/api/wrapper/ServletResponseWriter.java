package net.fze.common.std.api.wrapper;

import net.fze.common.std.api.ResponseWriter;

import javax.servlet.http.HttpServletResponse;

public class ServletResponseWriter implements ResponseWriter {
    private final HttpServletResponse rsp;

    public ServletResponseWriter(HttpServletResponse rsp) {
        this.rsp = rsp;
    }

    @Override
    public void addHeader(String name, String value) {
        this.rsp.addHeader(name, value);
    }

    @Override
    public void setStatus(int code) {
        this.rsp.setStatus(code);
    }

    @Override
    public void write(byte[] bytes) {
        try {
            this.rsp.getOutputStream().write(bytes);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
