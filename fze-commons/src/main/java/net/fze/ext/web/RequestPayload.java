package net.fze.ext.web;

import java.util.HashMap;

/**
 * 请求参数
 */
public class RequestPayload extends HashMap<String, Object> {
    public String getString(String key) {
        return (String) this.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }
}
