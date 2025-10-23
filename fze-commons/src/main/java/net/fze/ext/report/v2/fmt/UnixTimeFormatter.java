package net.fze.ext.report.v2.fmt;


import net.fze.ext.report.v2.FieldFormatterContext;
import net.fze.ext.report.v2.IExportFormatter;
import net.fze.util.Times;

public class UnixTimeFormatter implements IExportFormatter {
    @Override
    public Object formatField(FieldFormatterContext ctx) {
       long time = ctx.getLongValue();
       return Times.formatUnix(time,"yyyy/MM/dd HH:mm");
    }
}
