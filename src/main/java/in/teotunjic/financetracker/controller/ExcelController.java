package in.teotunjic.financetracker.controller;

import in.teotunjic.financetracker.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/expenses")
    public ResponseEntity<Resource> downloadExpenses() throws IOException {
        String filename = "expenses.xlsx";
        ByteArrayInputStream data = excelService.getExpenseData();
        InputStreamResource resource = new InputStreamResource(data);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);

    }

    @GetMapping("/incomes")
    public ResponseEntity<Resource> downloadIncomes() throws IOException {
        String filename = "incomes.xlsx";
        ByteArrayInputStream data = excelService.getIncomeData();
        InputStreamResource resource = new InputStreamResource(data);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);

    }
}
