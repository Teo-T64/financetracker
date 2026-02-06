package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.ExpenseDTO;
import in.teotunjic.financetracker.dto.IncomeDTO;
import in.teotunjic.financetracker.dto.RecentTransactionDTO;
import in.teotunjic.financetracker.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;

    private Map<String,Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrProfile();
        Map<String,Object> data = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes =  incomeService.getLatest5IncomesForCurrUser();
        List<ExpenseDTO> latestExpenses =  expenseService.getLatest5ExpensesForCurrUser();
        List<RecentTransactionDTO> recentTransactions =  Stream.concat(latestIncomes.stream().map(income-> RecentTransactionDTO.builder()
                .id(income.getId())
                .icon(income.getIcon())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .name(income.getName())
                .profileId(profile.getId())
                .amount(income.getAmount())
                .type("income")
                .build()), latestExpenses.stream().map(expense->RecentTransactionDTO.builder()
                    .id(expense.getId())
                    .icon(expense.getIcon())
                    .date(expense.getDate())
                    .createdAt(expense.getCreatedAt())
                    .updatedAt(expense.getUpdatedAt())
                    .name(expense.getName())
                    .profileId(profile.getId())
                    .amount(expense.getAmount())
                    .type("expense")
                    .build())).sorted((a,b)->{
                        int cmp = b.getDate().compareTo(a.getDate());
                        if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                            return b.getCreatedAt().compareTo(a.getCreatedAt());
                        }
                        return cmp;
        }).collect(Collectors.toList());
        data.put("totalBalance",incomeService.getTotalIncomeForCurrUser().subtract(expenseService.getTotalExpenseForCurrUser()));
        data.put("totalIncome",incomeService.getTotalIncomeForCurrUser());
        data.put("totalExpense",expenseService.getTotalExpenseForCurrUser());
        data.put("recent5Expenses",latestExpenses);
        data.put("recent5Incomes",latestIncomes);
        data.put("recentTransactions",recentTransactions);
        return data;
    }

}
