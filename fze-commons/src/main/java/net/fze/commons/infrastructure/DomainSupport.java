package net.fze.commons.infrastructure;

/**
 * 领域支持接口
 */
public interface DomainSupport {


    /**
     * 拼音
     *
     * @return 拼音
     */
    Pinyin4J pinyin();

    /**
     * 验证器
     *
     * @return 验证器
     */
    Validator validator();

    /**
     * 格式化工具
     *
     * @return 实例
     */
    Formatter formatter();

    /**
     * 生成器
     *
     * @return 生成器
     */
    Generator generator();

}
