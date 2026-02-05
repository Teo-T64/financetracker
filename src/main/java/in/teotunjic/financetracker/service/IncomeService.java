package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.IncomeDTO;
import in.teotunjic.financetracker.entity.CategoryEntity;
import in.teotunjic.financetracker.entity.IncomeEntity;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.IncomeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryService categoryService;
    private final IncomeRepo incomeRepo;

    public IncomeEntity toIncomeEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .profileEntity(profileEntity)
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
