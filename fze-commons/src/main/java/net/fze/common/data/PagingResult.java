package net.fze.common.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 * @author jarrysix
 */
public class PagingResult<T> {
    /**
     * 总数
     */
    @Schema(description = "总数")
    private long total;
    /**
     * 数据
     */
    @Schema(description = "数据")
    private List<T> rows;

    /**
     * 信息提示
     */
    @Schema(description = "信息提示")
    private String hint;

    public PagingResult() {

    }

    public PagingResult(long total, List<T> rows) {
        this.rows = rows;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    /**
     * 获取信息提示
     */
    public String getHint() {
        return hint;
    }

    /**
     * 设置信息提示
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * 返回空分页结果
     */
    public static PagingResult<?> emptyResult() {
        return new PagingResult<>(0, Collections.emptyList());
    }
}
