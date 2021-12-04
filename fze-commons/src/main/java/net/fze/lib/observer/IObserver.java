package net.fze.lib.observer;
/*
 created for report-server [ IObserver.java ]
 user: liuming (jarrysix@gmail.com)
 date: 29/12/2017 18:59
 description:
*/

/**
 * 监控器
 */
public interface IObserver {
    /**
     * 接收通知消息
     *
     * @param identity 通知信息标识
     * @param obj      数据
     * @return 错误
     */
    Error receive(String identity, Object obj);
}
