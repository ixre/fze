package net.fze.ext.web;

import net.fze.util.TypeConv;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 请求参数
 */
public class RequestPayload extends HashMap<String, Object> {
    public String getString(String key) {
        return (String) this.get(key);
    }

    public Integer getInt(String key) {
        return TypeConv.toInteger(this.get(key));
    }

    public Long getLong(String key) {
        return TypeConv.toLong(this.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public Double getDouble(String key) {
        return TypeConv.toDouble(this.get(key));
    }

    public Float getFloat(String key) {
        return TypeConv.toFloat(this.get(key));
    }

    public BigDecimal getBigDecimal(String key) {
        return TypeConv.toBigDecimal(this.get(key));
    }

}
