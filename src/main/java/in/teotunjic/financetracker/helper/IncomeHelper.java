package in.teotunjic.financetracker.helper;

import in.teotunjic.financetracker.dto.IncomeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class IncomeHelper {
    public static String[] HEADERS = {
            "Name", "Category", "Amount", "Date"
    };

    public static String SHEET_NAME = "income_data";

    public static ByteArrayInputStream dataToExcel(List<IncomeDTO> incomes) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);

                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (IncomeDTO incomeDTO : incomes) {
                Row dataRow = sheet.createRow(rowIndex++);

                dataRow.createCell(0).setCellValue(incomeDTO.getName());
                dataRow.createCell(1).setCellValue(incomeDTO.getCategoryName());

                if (incomeDTO.getAmount() != null) {
                    dataRow.createCell(2).setCellValue(incomeDTO.getAmount().doubleValue());
                }

                if (incomeDTO.getDate() != null) {
                    dataRow.createCell(3).setCellValue(incomeDTO.getDate().toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
        }
    }
}