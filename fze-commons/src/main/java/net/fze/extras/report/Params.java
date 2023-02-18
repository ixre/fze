package net.fze.extras.report;

import java.util.Map;

/** 参数 */
public class Params {
    private Map<String, Object> value;

    public Params(Map<String, Object> value) {
        this.value = value;
    }

    /** 获取值 */
    public Map<String, Object> getValue() {
        return this.value;
    }

    /** 从Map中拷贝数据 */
    public void copy(Map<String, Object> src) {
        src.forEach((k, v) -> {
            if (k.equals("total") || k.equals("rows") || k.equals("params")) {
                return;
            }
            this.value.put(k, v);
        });
    }

    /** 添加参数 */
    public void set(String key, Object value) {
        this.value.put(key, value);
    }

    /** 获取参数 */
    public Object get(String key) {
        return this.value.get(key);
    }

    /** 是否包含参数 */
    public boolean contains(String key) {
        return this.value.containsKey(key);
    }

    /** 删除参数 */
    public void remove(String key) {
        this.value.remove(key);
    }
}
