package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.ExpenseDTO;
import in.teotunjic.financetracker.dto.IncomeDTO;
import in.teotunjic.financetracker.helper.ExpenseHelper;
import in.teotunjic.financetracker.helper.IncomeHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExcelService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public ByteArrayInputStream getIncomeData() throws IOException {
        List<IncomeDTO> incomes = incomeService.getCurrMonthExpensesForCurrUser();
        return IncomeHelper.dataToExcel(incomes);
    }
    public ByteArrayInputStream getExpenseData() throws IOException {
        List<ExpenseDTO> expenses = expenseService.getCurrMonthExpensesForCurrUser();
        return ExpenseHelper.dataToExcel(expenses);
    }

}
