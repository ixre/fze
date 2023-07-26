package net.fze.ext.grpc;

import io.grpc.stub.StreamObserver;

/**
 * 相应构造器
 * @param <T> 返回类型
 */
public class ResponseBuilder<T> {

    private final StreamObserver<T> _instance;

    /**
     * 创建响应构造器
     * @param s 响应监听器
     * @return 响应构造器
     * @param <T> 响应类型
     */
    public static <T> ResponseBuilder<T> create(StreamObserver<T> s){
        return new ResponseBuilder<>(s);
    }

    private ResponseBuilder(StreamObserver<T> s){
        this._instance = s;
    }

    /**
     * 完成请求
     * @param t 响应对象
     */
    public void complete(T t){
        this._instance.onNext(t);
        this._instance.onCompleted();
    }

    /**
     * 请求错误
     * @param ex 异常
     */
    public void error(Throwable ex){
        this._instance.onError(ex);
        this._instance.onCompleted();
    }
}
