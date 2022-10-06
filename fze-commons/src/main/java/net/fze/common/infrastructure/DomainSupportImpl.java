package net.fze.common.infrastructure;

import com.google.inject.Singleton;
import net.fze.common.Standard;

@Singleton
public class DomainSupportImpl implements DomainSupport {

    private Pinyin4J _pinyin;
    private Formatter _formatter;
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
