package net.fze.ext.grpc;

import io.grpc.stub.StreamObserver;

/**
 * 服务方法
 *
 * @author jarrysix
 * @param <R> 服务接口
 * @param <P> 客户端
 */
public interface GrpcFunction<R, P> {
    void call(R r, StreamObserver<P> p);
}
