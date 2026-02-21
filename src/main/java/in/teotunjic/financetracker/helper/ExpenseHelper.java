package in.teotunjic.financetracker.helper;

import in.teotunjic.financetracker.dto.ExpenseDTO;
import in.teotunjic.financetracker.dto.IncomeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExpenseHelper {
    public static String[] HEADERS = {
            "Name", "Category", "Amount", "Date"
    };

    public static String SHEET_NAME = "expense_data";

    public static ByteArrayInputStream dataToExcel(List<ExpenseDTO> expenses) {
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
            for (ExpenseDTO expenseDTO : expenses) {
                Row dataRow = sheet.createRow(rowIndex++);

                dataRow.createCell(0).setCellValue(expenseDTO.getName());
                dataRow.createCell(1).setCellValue(expenseDTO.getCategoryName());

                if (expenseDTO.getAmount() != null) {
                    dataRow.createCell(2).setCellValue(expenseDTO.getAmount().doubleValue());
                }

                if (expenseDTO.getDate() != null) {
                    dataRow.createCell(3).setCellValue(expenseDTO.getDate().toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
        }
    }
}