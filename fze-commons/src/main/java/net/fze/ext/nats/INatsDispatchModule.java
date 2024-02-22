package net.fze.ext.nats;

import io.nats.client.Dispatcher;

/**
 * Nats消息分发订阅模块
 * @author jarrysix
 */
public interface INatsDispatchModule {
    /**
     * 配置分发订阅
     * @param dispatcher 分发器
     */
    void configure(Dispatcher dispatcher);
}
