package net.fze.ext.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * THRIFT服务加载器
 */
public class ThriftServe {
    /* 线程池服务 */
    private final TThreadPoolServer server;
    /* 服务处理器 */
    private final TMultiplexedProcessor processor;
    /**
     * 端口
     */
    private final int port;

    /**
     * 创建Thrift服务器
     *
     * @param port 端口
     * @throws TException 异常
     */
    public ThriftServe(int port) throws TException {
        // 服务传输方式
        TServerSocket serverTransport = new TServerSocket(port);
        // 创建协议工厂:TBinaryProtocol.Factory
        TProtocolFactory factory = new TBinaryProtocol.Factory();
        // 创建处理器
        this.processor = new TMultiplexedProcessor();
        // 服务
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        // 指定非阻塞
        args.transportFactory(new TFramedTransport.Factory());
        // 指定协议
        args.protocolFactory(factory);
        // 指定处理器
        args.processor(this.processor);
        this.port = port;
        this.server = new TThreadPoolServer(args);
    }

    /**
     * 运行Thrift服务
     */
    public void run() {
        System.out.println("[ Thrift][ Service] - observe thrift server on port " + this.port + " ...");
        this.server.serve();
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param serviceImpl 服务实现
     */
    public void register(String serviceName, TProcessor serviceImpl) {
        this.processor.registerProcessor(serviceName, serviceImpl);
    }
}
