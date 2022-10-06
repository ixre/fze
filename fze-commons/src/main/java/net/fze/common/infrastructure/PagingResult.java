package net.fze.common.infrastructure;

import java.util.List;

/**
 * 分页结果
 */
public class PagingResult<T> {
    /**
     * 总数
     */

    private long count;
    /**
     * 数据
     */
    private List<T> list;

    public PagingResult(List<T> list, long count) {
        this.list = list;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
