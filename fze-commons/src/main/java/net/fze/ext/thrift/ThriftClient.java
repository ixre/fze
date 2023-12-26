package net.fze.ext.thrift;
/*
 created for mzl-serve [ ThriftClient.java ]
 user: liuming (jarrysix@gmail.com)
 date: 09/12/2017 19:33
 description:
*/

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * THRIFT CLIENT
 *
 * @param <T> SERVICE CLIENT
 */
public class ThriftClient<T> {
    private final TTransport _transport;
    private T _client;

    public ThriftClient(TTransport t) {
        this._transport = t;
    }

    /**
     * 绑定客户端
     *
     * @param t 客户端
     */
    public ThriftClient<T> bind(T t) {
        this._client = t;
        return this;
    }

    /**
     * 获取客户端
     *
     * @return 客户端
     */
    public T get() {
        return this._client;
    }

    /**
     * 获取协议
     *
     * @param service 服务
     * @return 协议
     */
    public TProtocol getProtocol(String service) throws TTransportException {
        this._transport.open();
        TProtocol protocol = new TBinaryProtocol(this._transport);
        return new TMultiplexedProtocol(protocol, service);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (this._transport.isOpen()) {
            this._transport.close();
        }
    }
}
