package com.github.ixre.fze.commons.libs.thrift;

import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClientFactory {
    private final Boolean _framed;
    private String _url = "";
    private Integer _port = 0;

    protected ThriftClientFactory(String url, Integer port, Boolean framed) {
        this._url = url;
        this._port = port;
        this._framed = framed;
    }

    protected <T> ThriftClient<T> createClient() {
        if (_url == null || _url.length() == 0 || _port <= 0) {
            throw new Error("missing server url or port");
        }
        TTransport transport = new TSocket(_url, _port);
        if (this._framed) {
            transport = new TFramedTransport.Factory().getTransport(transport);
        }
        return new ThriftClient<>(transport);
    }
}
