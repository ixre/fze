package net.fze.ext.report.v2;


import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.util.Types;
import net.fze.util.tuple.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExportUtils {
    public static byte[] getExportData(Function<PagingParams, PagingResult<?>> ds,
                                       String exportFormat,
                                       List<Tuple2<String, String>> exportColumns,
                                       IExportFormatter... formatters) {
        List<Object> rows = fetchAllData(ds);
        List<Map<String, Object>> mapList = parseToMaps(rows);
        // 解析列头
        List<String> props = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        if (exportColumns != null && !exportColumns.isEmpty()) {
            exportColumns.forEach(c -> {
                props.add(c.getItem1());
                headers.add(c.getItem2());
            });
        } else {
            for (String key : mapList.get(0).keySet()) {
                props.add(key);
                headers.add(key.toUpperCase());
            }
        }
        // 格式化
        for(int i = 0;i < mapList.size();i++){
            Map<String,Object> row = mapList.get(i);
            Map<String,Object> deepCopy = new HashMap<>(row);
            for(String key : row.keySet()) {
                Object v = row.get(key);
                for (IExportFormatter fmt : formatters) {
                    v = fmt.formatField(new FieldFormatterContext(deepCopy,key,i));
                }
                row.put(key,v);
            }
            i++;
        }
        return ExportFactory.getStrategy(exportFormat)
                .getExportBytes(mapList, props, headers);
    }

    /**
     * 将行转为Map<String,Object>
     *
     * @param rows 行
     */
    private static List<Map<String, Object>> parseToMaps(List<Object> rows) {
        List<Map<String, Object>> mapRows = new ArrayList<>();
        for(int i=0;i<rows.size();i++) {
            Object row = rows.get(i);
            assert row != null;
            Map<String, Object> dst = null;
            if (row instanceof Map) {
                dst = (Map<String, Object>) row;
            }
            if (dst == null) {
                dst = Types.objectToMap(row);
            }
            dst.put("index", i+1);
            mapRows.add(dst);
        }
        return mapRows;
    }

    /**
     * 获取所有数据
     *
     * @param ds 数据源
     */
    private static List<Object> fetchAllData(Function<PagingParams, PagingResult<?>> ds) {
        List<Object> rows = new ArrayList<>();
        int pageIndex = 1;
        int pageSize = 100;
        while (true) {
            PagingResult<?> apply = ds.apply(PagingParams.of(pageIndex, pageSize));
            List<?> rows1 = apply.getRows();
            rows.addAll(rows1);
            pageIndex++;
            if (rows1.size() < pageSize) {
                break;
            }
        }
        return rows;
    }
}
