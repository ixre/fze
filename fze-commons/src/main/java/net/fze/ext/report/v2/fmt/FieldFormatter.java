package net.fze.ext.report.v2.fmt;


import net.fze.ext.report.v2.IExportFormatter;

/**
 * 字段格式化器
 */
public class FieldFormatter {
    private final String fieldName;
    private final IExportFormatter formatter;

    private FieldFormatter(String fieldName, IExportFormatter formatter) {
        this.fieldName = fieldName;
        this.formatter = formatter;
    }

    public static FieldFormatter of(String fieldName, IExportFormatter formatter) {
        return new FieldFormatter(fieldName, formatter);
    }

    public String getFieldName() {
        return fieldName;
    }

    public IExportFormatter getFormatter() {
        return formatter;
    }
}
