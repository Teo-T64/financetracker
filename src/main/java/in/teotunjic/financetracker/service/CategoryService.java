package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.CategoryDTO;
import in.teotunjic.financetracker.entity.CategoryEntity;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepo categoryRepo;

    private CategoryEntity toCategoryEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();

    }

    private CategoryDTO toCategoryDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile()!= null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .type(categoryEntity.getType())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();

    }

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrProfile();
        if(categoryRepo.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category with this name already exists!");
        }
        CategoryEntity newCategory = toCategoryEntity(categoryDTO,profile);
        newCategory =  categoryRepo.save(newCategory);
        return toCategoryDTO(newCategory);

    }

}
