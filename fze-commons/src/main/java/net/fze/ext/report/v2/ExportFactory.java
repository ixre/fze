package net.fze.ext.report.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportFactory {
    public static IExportFileStrategy getStrategy(String format) {
        switch (format) {
            case "excel":
            case "xlsx":
            case "xls":
                return new ExcelExportStrategy();
            case "csv":
                return new CsvExportStrategy();
            case "txt":
                return new TextExportStrategy();
        }
        throw new RuntimeException("not support export format");
    }

    public static ServletExporter getServletExporter(HttpServletRequest request, HttpServletResponse response) {
        return new ServletExporter(request,response);
    }
}
