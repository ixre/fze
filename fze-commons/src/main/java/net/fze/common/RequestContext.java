package net.fze.common;


import net.fze.ext.web.RequestPayload;

/**
 * 请求上下文
 */
public interface RequestContext<T> {
    T getContext();
}
