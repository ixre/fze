package net.fze.ext;

import net.fze.common.Context;
import net.fze.ext.hibernate.TinySession;

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
