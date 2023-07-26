package net.fze.ext.grpc;

import io.grpc.stub.StreamObserver;

/**
 * 服务方法
 * @param <R>
 * @param <P>
 */
public interface GrpcFunction<R,P> {
    void call(R r, StreamObserver<P> p);
}
