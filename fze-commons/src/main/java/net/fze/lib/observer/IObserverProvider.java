package net.fze.lib.observer;

/**
 * 观察提供者
 */
public interface IObserverProvider {
    /**
     * 启动
     */
    void observe();

    /**
     * 注册观察程序
     *
     * @param key 键
     * @param obs 观察者
     */
    void register(String key, IObserver obs);

    /**
     * 记录错误
     *
     * @param key 队列键
     * @param val 对列值
     * @param err 错误
     */
    void handleError(String key, String val, Error err);
}
