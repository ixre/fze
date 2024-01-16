package net.fze.ext.sse;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 服务器事件代理接口
 */
public interface IServerEventProvider {

    /**
     * 创建SSE连接
     */
    AsyncContext connect(String topic);


    /**
     * 创建SSE连接
     */
    AsyncContext connect(String topic, HttpServletRequest request);

    /**
     * 推送消息
     *
     * @param topic 主题
     * @param data  数据
     */
    void push(String topic, String data);

    /**
     * 推送消息
     *
     * @param topic  主题
     * @param data   数据
     * @param filter 筛选符合条件的上下文进行推送
     */
    void push(String topic, String data, IAsyncContextFilter filter);
}
