package net.fze.common.std.api;

/**
 * 中间件
 */
public interface MiddlewareFunc {
    Error handle(Context ctx);
}
