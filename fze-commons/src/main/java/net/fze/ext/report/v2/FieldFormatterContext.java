package net.fze.ext.report.v2;

import net.fze.util.TypeConv;
import net.fze.util.Types;

import java.util.Map;

/**
 * 导出行
 */
public class FieldFormatterContext {
    private final Map<String, Object> row;
    private final int index;
    private final String currentKey;

    public FieldFormatterContext(Map<String, Object> row, String currentKey,int index) {
        this.row = row;
        this.currentKey = currentKey;
        this.index = index;
    }


    public Map<String, Object> getRow() {
        return row;
    }

    public Integer getIntValue(String field){
        return TypeConv.toInt(this.getFieldValue(field));
    }
    public String getString(String field){
        return TypeConv.toString(this.getFieldValue(field));
    }
    public Double getDouble(String field){
        return TypeConv.toDouble(this.getFieldValue(field));
    }
    public Long getLongValue(String field){
        return TypeConv.toLong(this.getFieldValue(field));
    }
    public Boolean getBoolean(String field){
        return TypeConv.toBoolean(this.getFieldValue(field));
    }
    public Object getFieldValue(String field){
        return row.get(Types.orValue(field,currentKey));
    }

    public int getIndex() {
        return index;
    }

    public Object getFieldValue() {
       return this.getFieldValue(null);
    }

    public int getIntValue() {
        return this.getIntValue(null);
    }

    public long getLongValue() {
        return this.getLongValue(null);
    }
}
