package in.teotunjic.financetracker.repo;

import in.teotunjic.financetracker.entity.ExpenseEntity;
import in.teotunjic.financetracker.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepo extends JpaRepository<IncomeEntity,Long> {
    List<IncomeEntity> findByProfileIdOrderByDesc(Long profileId);
    List<IncomeEntity> findTop5ByProfileIdOrderByDesc(Long profileId);
    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profileEntity.id = :profileId ")
    BigDecimal findTotalSumProfileId(@Param("profileId") Long profileId);
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String name, Sort sort);
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

}
