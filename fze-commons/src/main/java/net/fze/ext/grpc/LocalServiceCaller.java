package net.fze.ext.grpc;

import net.fze.ext.injector.InjectFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 本地直接调用RPC服务实现(不通过端口)
 * @param <S>
 *
 *  MemberLogin.Builder builder = MemberLogin.newBuilder();
 *  builder.setPhone("13800000001")
 *     .setPassword("8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414")
 *     .setBrandType(2)
 *     .setPortalType(1);
 *  MemberLoginResponse ret = LocalServiceCaller.of(MemberServiceGrpcImpl.class)
 *     .call(s -> s::checkLogin, builder.build());
 *  System.out.println("----"+Types.toJson(ret));
 */
public class LocalServiceCaller<S> {

    private final S _instance;

    /**
     * 自动获取服务实例
     * @param clazz 服务类型
     */
    private LocalServiceCaller(Class<S> clazz) {
        this._instance = InjectFactory.getInstance(clazz);
    }

    /**
     * 外部传入实例
     * @param instance 实例
     */
    private LocalServiceCaller(S instance) {
        this._instance = instance;
    }

    /**
     * 使用类型创建服务调用实例
     * @param clazz 类型
     * @return 服务调用对象
     * @param <S> 服务类型
     */
    public static <S> LocalServiceCaller<S> of(Class<S> clazz) {
        return new LocalServiceCaller<>(clazz);
    }

    /**
     * 使用类型创建服务调用实例
     * @param instance 实例
     * @return 服务调用对象
     * @param <S> 服务类型
     */
    public static <S> LocalServiceCaller<S> of(S instance) {
        return new LocalServiceCaller<>(instance);
    }

    /**
     * 调用方法
     * @param f 方法
     * @param r 请求参数
     * @return 响应结果
     */
    public <R,P> P call(Function<S, GrpcFunction<R,P>> f,R r) {
        AtomicReference<P> p = new AtomicReference<>();
        f.apply(this._instance).call(r,new ConsumerStreamObserver<>(p::set));
        return p.get();
    }
}
