package net.fze.component;

import net.fze.commons.Context;
import net.fze.component.hibernate.TinySession;

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
