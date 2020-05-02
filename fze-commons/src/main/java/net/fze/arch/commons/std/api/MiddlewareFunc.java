package net.fze.arch.commons.std.api;

/**
 * 中间件
 */
public interface MiddlewareFunc {
    Error handle(Context ctx);
}
