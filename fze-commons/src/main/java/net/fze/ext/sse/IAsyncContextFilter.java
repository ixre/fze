package net.fze.ext.sse;

import javax.servlet.AsyncContext;

/**
 * 筛选异步请求上下文，常用于针对指定请求做响应处理
 */
public interface IAsyncContextFilter {
    boolean filter(AsyncContext ctx);
}
