package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.CashOutflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CashOutflowRepository extends JpaRepository<CashOutflow, Long> {

    @Query("""
        SELECT o
        FROM CashOutflow o
        WHERE o.occurredAt BETWEEN :start AND :end
        ORDER BY o.occurredAt DESC
    """)
    List<CashOutflow> findAllByOccurredAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
        SELECT COALESCE(SUM(o.amount), 0)
        FROM CashOutflow o
        WHERE o.occurredAt BETWEEN :start AND :end
    """)
    BigDecimal sumAmountByOccurredAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
