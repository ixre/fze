package net.fze.ext.report.v2;


import net.fze.util.TypeConv;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ExcelExportStrategy implements IExportFileStrategy {
    protected ExcelExportStrategy() {

    }

    @Override
    public byte[] getExportBytes(List<Map<String, Object>> mapList, List<String> props, List<String> headers) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            // Create data rows
            for (int i = 0; i < mapList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> rowData = mapList.get(i);

                for (int j = 0; j < props.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = rowData.get(props.get(j));
                    cell.setCellValue(value != null ? TypeConv.toString(value) : "");

                    // 设置列宽度
                    if(i == 0){
                        int len = Math.max(headers.get(j).length(),TypeConv.toString(value).length());
                        sheet.setColumnWidth(j, (Math.max(len,5)+5) * 256);
                    }
                }
            }
            // Write to byte array
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel file", e);
        }
    }
}
