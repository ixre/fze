package net.fze.common.infrastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页参数
 */
public class PagingParams {
    /**
     * 参数
     */
    private Map<String, String> opt; // required
    /**
     * 排序字段
     */
    private String orderField; // required
    /**
     * 是否倒序排列
     */
    private boolean orderDesc; // required
    /**
     * 开始记录数
     */
    private int begin; // required
    /**
     * 结束记录数
     */
    private int over; // required

    /**
     * 修正参数,去除某些空的参数
     *
     * @param opt 分页参数
     */
    public static PagingParams fix(PagingParams opt) {
        if (opt == null) opt = new PagingParams();
        if (opt.getOrderField() == null) opt.setOrderField("");
        if (opt.getOpt() == null) {
            opt.setOpt(new HashMap<>());
        } else {
            for (Map.Entry<String, String> e : opt.getOpt().entrySet()) {
                if (e.getValue() == null) opt.getOpt().remove(e.getKey());
            }
        }
        return opt;
    }

    public Map<String, String> getOpt() {
        return this.opt;
    }

    public void setOpt(Map<String, String> opt) {
        this.opt = opt;
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

    public int getBegin() {
        return this.begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getOver() {
        return this.over;
    }

    public void setOver(int over) {
        this.over = over;
    }
}
