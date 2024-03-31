/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : IEventBusStore.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-03-31 09:35
 * description :
 * history :
 */
package net.fze.domain.event;

/**
 * 事件总线持久化存储
 * @author jarrysix
 */
public interface IEventBusStore {

    /**
     * 存储事件数据
     * @param eventId 事件ID
     * @param eventName 事件名称
     * @param event 事件数据
     */
    void storeEvent(String eventId,String eventName,  Object event);

    /**
     * 捕获异常
     *
     * @param eventId   事件ID
     * @param eventName 事件名称
     * @param ex        异常
     */
    void captureException(String eventId, String eventName, Throwable ex);
}