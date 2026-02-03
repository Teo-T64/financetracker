package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepo categoryRepo;

}
