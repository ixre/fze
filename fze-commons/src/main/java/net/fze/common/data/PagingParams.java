package net.fze.common.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页参数
 */
public class PagingParams {
    /**
     * 参数
     */
    @Schema(description = "参数")
    private final Map<String, String> params;

    /**
     * 页码索引
     */
    @Schema(description = "页码索引")
    private int pageIndex;

    /**
     * 页码条数
     */
    @Schema(description = "页码条数")
    private int pageSize;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String orderField;

    /**
     * 是否倒序排列
     */
    @Schema(description = "是否倒序排列")
    private boolean orderDesc;


    PagingParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 创建分页参数
     * @param pageIndex 页码索引
     * @param pageSize  页码条数
     * @param params    参数
     * @return 参数
     */
    public static PagingParams of(int pageIndex, int pageSize, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        PagingParams p = new PagingParams(params);
        p.pageIndex = pageIndex;
        p.pageSize = pageSize;
        return fix(p);
    }

    /**
     * 修正参数,去除某些空的参数
     *
     * @param opt 分页参数
     */
    static PagingParams fix(PagingParams opt) {
        if (opt.getOrderField() == null) {
            opt.setOrderField("");
        }
        if (opt.getParams() != null) {
            for (Map.Entry<String, String> e : opt.getParams().entrySet()) {
                if (e.getValue() == null) {
                    opt.getParams().remove(e.getKey());
                }
            }
        }
        return opt;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getOrderField() {
        return this.orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public boolean getOrderDesc() {
        return this.orderDesc;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getPageSize() {
        return this.pageSize;
    }

}
