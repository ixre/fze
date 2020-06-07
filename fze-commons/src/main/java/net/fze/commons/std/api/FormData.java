package net.fze.commons.std.api;

import net.fze.commons.Types;
import net.fze.commons.TypesConv;

import java.util.HashMap;
import java.util.Map;

/**
 * 表单数据
 */
public class FormData {
    private Map<String, Object> data = new HashMap<>();

    public FormData() {
    }

    public FormData(Map<String, Object> data) {
        Types.copyMap(this.data, data);
    }

    public boolean container(String key) {
        return this.data.containsKey(key);
    }

    public Object get(String key) {
        if (this.container(key)) {
            return this.data.get(key);
        }
        return null;
    }

    public int getInt(String key) {
        Object o = this.get(key);
        if (o != null) {
            if (o.equals("")) o = "0";
            return TypesConv.toInt(o);
        }
        return 0;
    }

    public byte[] getBytes(String key) {
        return this.getString(key).getBytes();
    }

    // 获取字符串
    public String getString(String key) {
        Object o = this.get(key);
        if (o != null) {
            return TypesConv.toString(o);
        }
        return "";
    }

    // 获取浮点数
    public float getFloat(String key) {
        Object o = this.get(key);
        if (o != null) {
            if (o.equals("")) o = "0";
            return TypesConv.toFloat(o);
        }
        return 0F;
    }

    // 获取布尔值
    public boolean getBool(String key) {
        Object o = this.get(key);
        if (o != null) {
            return o.equals("True") || o.equals("true") || o.equals("1");
        }
        return false;
    }

    public void set(String key, Object value) {
        this.data.put(key, value);
    }
}
