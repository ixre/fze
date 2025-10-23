package net.fze.ext.report.v2.fmt;


import net.fze.ext.report.v2.FieldFormatterContext;
import net.fze.ext.report.v2.IExportFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表格式化
 */
public class ReportFormatterRegistry {
    private static final Map<String, IExportFormatter> formatterMap = new HashMap<>();
    private static final Object locker = new Object();
    public static void register(String fieldName,IExportFormatter formatter){
        synchronized (locker){
            formatterMap.put(fieldName,formatter);
        }
    }

    public static void register(Map<String,IExportFormatter> formatters){
        synchronized (locker){
            formatterMap.putAll(formatters);
        }
    }

    public static void registerByFormat(String field,FieldFormats fmt){
        switch (fmt){
            case UNIX_TIME:
                register(field,new UnixTimeFormatter());
                break;
        }
    }
    public static IExportFormatter getFormatter(String name){
        return formatterMap.get(name);
    }
    public static void unregister(String name){
        formatterMap.remove(name);
    }

    public static void resolveRows(List<?> rows, FieldFormatter ... formatters) {
        for (int i = 0; i < rows.size(); i++) {
            Object row = rows.get(i);
            Map<String, Object> map = (Map<String, Object>) (row);
            int index = i;
            map.keySet().forEach(key -> {
                for (FieldFormatter formatter : formatters) {
                    if (formatter.getFieldName().equals(key)) {
                        Object o = formatter.getFormatter().formatField(new FieldFormatterContext(map, key, index));
                        if(o != null) {
                            map.put(key, o);
                        }
                        return;
                    }
                }
                IExportFormatter formatter = getFormatter(key);
                if (formatter != null) {
                    Object o = formatter.formatField(new FieldFormatterContext(map, key, index));
                    if(o != null) {
                        map.put(key, o);
                    }
                }
            });
        }
    }

    public static void registerUnixTimeFormatters(String... fieldNames) {
        for (String fieldName : fieldNames) {
            registerByFormat(fieldName, FieldFormats.UNIX_TIME);
        }
    }
}
