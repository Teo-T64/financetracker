package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.ExpenseDTO;
import in.teotunjic.financetracker.entity.CategoryEntity;
import in.teotunjic.financetracker.entity.ExpenseEntity;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.CategoryRepo;
import in.teotunjic.financetracker.repo.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepo categoryRepo;
    private final ExpenseRepo expenseRepo;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO){
        ProfileEntity profile = profileService.getCurrProfile();
        CategoryEntity category =  categoryRepo.findById(expenseDTO.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        ExpenseEntity newExpense =  toExpenseEntity(expenseDTO,profile,category);
        newExpense = expenseRepo.save(newExpense);
        return toExpenseDTO(newExpense);
    }

    public ExpenseEntity toExpenseEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .profile(profileEntity)
                .categoryEntity(categoryEntity)
                .build();
    }

    public ExpenseDTO toExpenseDTO(ExpenseEntity expenseEntity){
        return ExpenseDTO.builder()
                .name(expenseEntity.getName())
                .id(expenseEntity.getId())
                .icon(expenseEntity.getIcon())
                .categoryId(expenseEntity.getCategoryEntity() != null ? expenseEntity.getCategoryEntity().getId() : null)
                .categoryName(expenseEntity.getCategoryEntity() != null ?  expenseEntity.getCategoryEntity().getName() : "N/A")
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }

}
