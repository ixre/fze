package net.fze.common.std.api;

import java.io.IOException;

// API服务
public interface Server {
    // 注册客户端
    void register(String name, Processor p);

    // adds middleware
    void use(MiddlewareFunc fn);

    // adds after middleware
    void After(MiddlewareFunc fn);

    // trace mode
    boolean Trace();

    // serve http
    void ServeHTTP(ResponseWriter rsp, Request req) throws IOException;
}
