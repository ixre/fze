package net.fze.ext.report.v2;

import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.util.Types;
import net.fze.util.tuple.Tuple;
import net.fze.util.tuple.Tuple2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ServletExporter {
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    protected ServletExporter(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    public void export(Function<PagingParams, PagingResult<?>> ds){
        String exportFormat = Types.orValue(request.getParameter("exportFormat"),"xlsx");
        String exportColumns = request.getParameter("exportColumns");
        List< Tuple2<String,String>> columns = this.resolveExportColumns(exportColumns);
        byte[] exportData = ExportUtils.getExportData(ds, exportFormat, columns);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(exportData.length);
        try {
            response.getOutputStream().write(exportData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Tuple2<String, String>> resolveExportColumns(String exportColumns) {
        if (exportColumns == null || exportColumns.isEmpty()) {
            return new ArrayList<>();
        }
        String[] columns = exportColumns.split(",");
        int len = columns.length;
        List<Tuple2<String, String>> result = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            String[] arr = columns[i].split(":");
            if (arr.length != 2) {
                return null;
            }
            result.add(Tuple.of(arr[0], arr[1]));
        }
        return result;
    }
}
