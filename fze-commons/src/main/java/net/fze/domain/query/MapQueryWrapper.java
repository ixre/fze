package net.fze.domain.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Map通用查询条件
 */
public class MapQueryWrapper implements IQueryWrapper{
    private final Map<String, Object> params = new HashMap<>();

    public MapQueryWrapper() {
    }

    public MapQueryWrapper(Map<String, Object> params) {
        this.params.putAll(params);
    }

    /**
     * 获取查询参数
     * @return 查询参数
     */
    public Map<String, Object> getQueryParams() {
        return params;
    }
    public MapQueryWrapper add(String key, Object value) {
        params.put(key, value);
        return this;
    }
}
