package net.fze.ext.grpc;

import io.grpc.stub.StreamObserver;

import java.util.function.Consumer;

/**
 * 本地调用消费监听器
 *
 * @param <T>
 */
class ConsumerStreamObserver<T> implements StreamObserver<T> {

    private final Consumer<T> _consumer;

    public ConsumerStreamObserver(Consumer<T> consumer) {
        this._consumer = consumer;
    }

    @Override
    public void onNext(T value) {
        this._consumer.accept(value);
    }


    @Override
    public void onError(Throwable t) {
        throw new RuntimeException(t);
    }

    /**
     * Receives a notification of successful stream completion.
     *
     * <p>May only be called once and if called it must be the last method called. In particular if an
     * exception is thrown by an implementation of {@code onCompleted} no further calls to any method
     * are allowed.
     */
    @Override
    public void onCompleted() {

    }
}
