package net.fze.arch.commons.infrastructure;

import com.google.inject.Singleton;
import net.fze.arch.commons.std.Standard;

@Singleton
public class DomainSupportImpl implements DomainSupport {

    private Pinyin4J _pinyin;
    private Formatter _formatter;
    private Validator _validator;
    private Generator _generator;


    public Pinyin4J pinyin() {
        if (this._pinyin == null) {
            this._pinyin = new Pinyin4J();
        }
        return this._pinyin;
    }

    public Formatter formatter() {
        if (this._formatter == null) {
            this._formatter = new Formatter();
        }
        return this._formatter;
    }

    /**
     * 验证器
     *
     * @return 验证器
     */
    public Validator validator() {
        if (this._validator == null) {
            this._validator = new Validator(Standard.std);
        }
        return this._validator;
    }

    /**
     * 生成器
     *
     * @return 生成器
     */
    public Generator generator() {
        if (this._generator == null) {
            this._generator = new Generator(Standard.std);
        }
        return this._generator;
    }

}
