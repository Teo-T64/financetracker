package in.teotunjic.financetracker.repo;

import in.teotunjic.financetracker.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long> {
    List<ExpenseEntity> findByProfileIdOrderByDesc(Long profileId);
    List<ExpenseEntity> findTop5ByProfileIdOrderByDesc(Long profileId);
    @Query("SELECT SUM(i.amount) FROM ExpenseEntity i WHERE i.profileEntity.id = :profileId ")
    BigDecimal findTotalSumProfileId(@Param("profileId") Long profileId);
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate,String name, Sort sort);
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
