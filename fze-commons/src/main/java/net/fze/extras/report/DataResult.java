package net.fze.extras.report;

import java.util.List;
import java.util.Map;

/** 数据结果 */
public class DataResult {
    private List<Map<String, Object>> rows;
    private List<Map<String, Object>> sub;
    private int total;
    private String err;

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public List<Map<String, Object>> getSub() {
        return sub;
    }

    public void setSub(List<Map<String, Object>> sub) {
        this.sub = sub;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
