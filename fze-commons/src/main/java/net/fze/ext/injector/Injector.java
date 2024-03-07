package net.fze.ext.injector;

/**
 * 注入器接口
 * @author jarrysix
 */
public interface Injector {
    /**
     * 获取实例
     *
     * @param c   类型
     * @param <T> 类型
     * @return 实例
     */
    <T> T getInstance(Class<T> c);
}
