package net.fze.ext.report.v2;

/**
 * 列格式化
 */
public interface IExportFormatter {
    /**
     * 格式化
     *
     * @param ctx   上下文
     * @return 格式化后的值
     */
    Object formatField(FieldFormatterContext ctx);
}
