package com.github.ixre.fze.web.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 轻量级的HTTP Server
 */
public class LightHttpServer {
    private final HttpServer server;

    private LightHttpServer(InetSocketAddress addr, int backlog) throws IOException {
        this.server = HttpServer.create(addr, backlog);
        // this.server.setExecutor(null); // 单线程
        this.server.setExecutor(Executors.newCachedThreadPool());
    }

    public static LightHttpServer create(InetSocketAddress addr, int backlog) throws IOException {
        return new LightHttpServer(addr, backlog);
    }

    public void route(String path, Handler h) {
        this.server.createContext(
                path,
                ex -> {
                    Error err;
                    HttpContext ctx = new HttpContext(ex);
                    try {
                        err = h.serveHttp(ctx);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        err = new Error(t.getMessage());
                    }
                    if (err != null) {
                        ctx.response().errorOutput(err);
                    }
                    ex.close();
                });
    }

    /**
     * 开启服务
     */
    public void start() {
        this.server.start();
    }

    /**
     * 停止服务
     */
    public void stop() {
        this.server.stop(1000);
    }

    /**
     * 设置模板
     */
    public void setTemplate(String freeMaker, String tpPath, String suffix) {
        if (freeMaker.equals("FreeMaker")) {
            FreeMakerLoader.configure(tpPath, suffix);
        }
    }

    /**
     * 静态目录
     */
    public void staticFile(String prefix, String directory) {
        this.route(prefix, new StaticFile(directory));
    }
}
