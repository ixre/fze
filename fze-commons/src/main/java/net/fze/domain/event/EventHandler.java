/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : EventHandler.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-03-31 15:02
 * description :
 * history :
 */
package net.fze.domain.event;


/**
 * 事件处理程序
 * @author jarrysix
 */
public interface EventHandler<T> {
    void handle(T t);
}