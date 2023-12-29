package net.fze.ext.thrift;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

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
        if (_url == null || _url.isEmpty() || _port <= 0) {
            throw new Error("missing server url or port");
        }
        TTransport transport = null;
        try {
            transport = new TSocket(_url, _port);
            if (this._framed) {
                transport = new TFramedTransport.Factory().getTransport(transport);
            }
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
        return new ThriftClient<>(transport);
    }
}
