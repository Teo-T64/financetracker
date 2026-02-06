package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.IncomeDTO;
import in.teotunjic.financetracker.entity.CategoryEntity;
import in.teotunjic.financetracker.entity.IncomeEntity;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.CategoryRepo;
import in.teotunjic.financetracker.repo.IncomeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepo categoryRepo;
    private final IncomeRepo incomeRepo;
    private final ProfileService profileService;

    public IncomeDTO addIncome(IncomeDTO incomeDTO){
        ProfileEntity profile = profileService.getCurrProfile();
        CategoryEntity category =  categoryRepo.findById(incomeDTO.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        IncomeEntity newExpense =  toIncomeEntity(incomeDTO,profile,category);
        newExpense = incomeRepo.save(newExpense);
        return toIncomeDTO(newExpense);
    }

    public void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrProfile();
        IncomeEntity toDelete =  incomeRepo.findById(incomeId)
                .orElseThrow(()-> new RuntimeException("Income not found"));
        if(!(toDelete.getProfile().getId().equals(profile.getId()))){
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepo.delete(toDelete);
    }


    public List<IncomeDTO> getLatest5IncomesForCurrUser(){
        ProfileEntity profile  = profileService.getCurrProfile();
        List<IncomeEntity> incomes =  incomeRepo.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
        return incomes.stream().map(this::toIncomeDTO).toList();
    }

    public BigDecimal getTotalIncomeForCurrUser(){
        ProfileEntity profile  = profileService.getCurrProfile();
        BigDecimal total = incomeRepo.findTotalSumProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<IncomeDTO> getCurrMonthExpensesForCurrUser(){
        ProfileEntity profile = profileService.getCurrProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate =  now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> incomes =  incomeRepo.findByProfile_IdAndDateBetween(profile.getId(), startDate,endDate);
        return incomes.stream().map(this::toIncomeDTO).toList();
    }

    public IncomeEntity toIncomeEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .profile(profileEntity)
                .categoryEntity(categoryEntity)
                .build();
    }

    public IncomeDTO toIncomeDTO(IncomeEntity incomeEntity){
        return IncomeDTO.builder()
                .name(incomeEntity.getName())
                .id(incomeEntity.getId())
                .icon(incomeEntity.getIcon())
                .categoryId(incomeEntity.getCategoryEntity() != null ? incomeEntity.getCategoryEntity().getId() : null)
                .categoryName(incomeEntity.getCategoryEntity() != null ?  incomeEntity.getCategoryEntity().getName() : "N/A")
                .amount(incomeEntity.getAmount())
                .date(incomeEntity.getDate())
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}
