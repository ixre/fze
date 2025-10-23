package net.fze.ext.report.v2;

import java.util.List;
import java.util.Map;


/**
 * 导出csv
 */
public class CsvExportStrategy implements IExportFileStrategy {
    protected CsvExportStrategy() {
    }


    private final String delimiter = ",";


    private void appendField(StringBuilder buf, int index, Object data) {
        if (index > 0) {
            buf.append(delimiter);
        }

        String dataStr = data.toString();
        boolean specData = dataStr.contains(" ") ||
                dataStr.contains("-") ||
                dataStr.contains("'");

        if (dataStr.contains("\"")) {
            dataStr = dataStr.replace("\"", "\"\"");
            specData = true;
        }

        if (specData) {
            buf.append("\"").append(dataStr).append("\"");
        } else {
            buf.append(dataStr);
        }
    }

    @Override
    public byte[] getExportBytes(List<Map<String, Object>> rows, List<String> fields, List<String> headers) {
        StringBuilder buf = new StringBuilder();

        // Show headers if available
        boolean showHeader = !headers.isEmpty();
        if (showHeader) {
            for (int i = 0; i < headers.size(); i++) {
                if (i > 0) {
                    buf.append(delimiter);
                }
                buf.append(headers.get(i));
            }
        }

        // Write data rows
        for (int i = 0; i < rows.size(); i++) {
            if (showHeader || i > 0) {
                buf.append("\r\n");
            }

            Map<String, Object> row = rows.get(i);
            for (int fi = 0; fi < fields.size(); fi++) {
                String field = fields.get(fi);
                appendField(buf, fi, row.get(field));
            }
        }

        return buf.toString().getBytes();

    }
}