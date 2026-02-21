package in.teotunjic.financetracker.controller;

import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.service.EmailService;
import in.teotunjic.financetracker.service.ExcelService;
import in.teotunjic.financetracker.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final ProfileService profileService;
    private final ExcelService excelService;

    @GetMapping("/income-excel")
    public ResponseEntity<String> incomeDataEmail() {
        try {
            ProfileEntity profile = profileService.getCurrProfile();

            byte[] excelContent = excelService.getIncomeData().readAllBytes();

            String subject = "Your Income Report";
            String body = "Hello " + profile.getFullName() + ",\n\n" +
                    "As requested, please find your income report attached.";
            String fileName = "Income_Report.xlsx";

            emailService.sendEmailWithAttachment(
                    profile.getEmail(),
                    subject,
                    body,
                    excelContent,
                    fileName
            );

            return ResponseEntity.ok("Email sent successfully to " + profile.getEmail());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to generate Excel file.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/expense-excel")
    public ResponseEntity<String> expenseDataEmail() {
        try {
            ProfileEntity profile = profileService.getCurrProfile();

            byte[] excelContent = excelService.getExpenseData().readAllBytes();

            String subject = "Your Income Report";
            String body = "Hello " + profile.getFullName() + ",\n\n" +
                    "As requested, please find your expense report attached.";
            String fileName = "Expense_Report.xlsx";

            emailService.sendEmailWithAttachment(
                    profile.getEmail(),
                    subject,
                    body,
                    excelContent,
                    fileName
            );

            return ResponseEntity.ok("Email sent successfully to " + profile.getEmail());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to generate Excel file.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

}

