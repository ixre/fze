package com.github.ixre.fze.commons.std.api;

public interface ResponseWriter {
    void addHeader(String s, String s1);

    void setStatus(int i);

    void write(byte[] bytes);
}
