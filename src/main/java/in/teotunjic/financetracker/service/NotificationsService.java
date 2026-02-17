package in.teotunjic.financetracker.service;


import in.teotunjic.financetracker.dto.ExpenseDTO;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.ProfileRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationsService {
    private final ProfileRepo profileRepo;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${finance.tracker.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "CET")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> users =  profileRepo.findAll();
        for(ProfileEntity profile : users){
            String body = "Hello " + profile.getFullName() + ",<br></br>" +
                    "This is a reminder for adding your income/expense for this day in FinanceTracker.<br></br>" +
                    "<a href="+frontendUrl+">Go To FinanceTracker</a>" +
                    "<br></br>Best regards, Finance Tracker Team.";
            emailService.sendEmail(profile.getEmail(), "Daily FinanceTracker Reminder", body);
        }
        log.info("Job ended: sendDailyIncomeExpenseReminder()");

    }

    @Scheduled(cron = "0 0 23 * * *",zone = "CET")
    public void sendDailyExpenseSummary(){
        log.info("Job started: sendDailyExpenseSummary()");
        List<ProfileEntity> users =  profileRepo.findAll();
        for(ProfileEntity profile : users){
           List<ExpenseDTO> list = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
           if(!list.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr><th style='border:1px solid; padding:5px;'>S.No</th><th style='border:1px solid;padding:5px;'>Name</th><th style='border:1px solid;padding:5px;'>Amount</th><th style='border:1px solid;padding:5px;'>Category</th></tr>");
                int i = 1;
                for(ExpenseDTO expenseDTO: list){
                    table.append("<tr>");
                    table.append("<td style='border 1px solid #ddd; padding:5px; '>").append(i++).append("</td>");
                    table.append("<td style='border 1px solid #ddd; padding:5px; '>").append(expenseDTO.getName()).append("</td>");
                    table.append("<td style='border 1px solid #ddd; padding:5px; '>").append(expenseDTO.getAmount()).append("</td>");
                    table.append("<td style='border 1px solid #ddd; padding:5px; '>").append(expenseDTO.getCategoryId() != null ?  expenseDTO.getCategoryName() : "N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hello " + profile.getFullName() + ",<br></br>" + "Here is the summary of your expenses for today:<br></br>" +
                        table + "<br></br>";
                emailService.sendEmail(profile.getEmail(), "Expense Summary", body);

           }
        }
        log.info("Job ended: sendDailyExpenseSummary()");


    }
}
