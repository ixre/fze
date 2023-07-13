package net.fze.common.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页参数
 */
public class PagingParams {
    /**
     * 参数
     */
    private final Map<String, String> params; // required

    /**
     * 页码索引
     */
    private int pageIndex; // required

    /**
     * 页码条数
     */
    private int pageSize; // required
    /**
     * 排序字段
     */
    private String orderField; // required
    /**
     * 是否倒序排列
     */
    private boolean orderDesc; // required


    public static PagingParams of(int pageIndex,int pageSize,Map<String, String> params){
        if(params == null){
            params =new HashMap<>();
        }
        PagingParams p = new PagingParams(params);
        p.pageIndex = pageIndex;
        p.pageSize = pageSize;
        return fix(p);
    }

    PagingParams(Map<String, String> params){
        this.params = params;
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
                if (e.getValue() == null)
                    opt.getParams().remove(e.getKey());
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
