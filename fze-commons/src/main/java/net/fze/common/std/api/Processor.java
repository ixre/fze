package net.fze.common.std.api;


// 处理器
public interface Processor {
    /**
     * 请求
     *
     * @param fn  方法
     * @param ctx 上下文
     * @return 响应
     */
    Response request(String fn, Context ctx);
}

