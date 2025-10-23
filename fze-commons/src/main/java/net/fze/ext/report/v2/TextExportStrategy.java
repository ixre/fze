package net.fze.ext.report.v2;

import java.util.List;
import java.util.Map;

public class TextExportStrategy implements IExportFileStrategy {
    private final CsvExportStrategy csv;

    protected TextExportStrategy() {
        this.csv = new CsvExportStrategy();

    }

    @Override
    public byte[] getExportBytes(List<Map<String, Object>> mapList, List<String> props, List<String> headers) {
        return this.csv.getExportBytes(mapList,props,headers);
    }
}
