package in.teotunjic.financetracker.repo;

import in.teotunjic.financetracker.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepo extends JpaRepository<ProfileEntity,Long> {

    Optional<ProfileEntity>findByEmail(String email);
}
