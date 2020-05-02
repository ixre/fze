package net.fze.arch.component;

import net.fze.arch.commons.Context;
import net.fze.arch.component.hibernate.TinySession;

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
