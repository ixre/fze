package com.github.ixre.fze.component;

import com.github.ixre.fze.commons.Context;
import com.github.ixre.fze.component.hibernate.TinySession;

/**
 * 扩展的上下文
 */
public interface XContext extends Context {
    /**
     * 获取会话
     *
     * @return Hibernate会话
     */
    TinySession hibernate();
}
