package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.CashBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
    boolean existsByBusinessDate(LocalDate businessDate);
    Optional<CashBox> findByBusinessDate(LocalDate businessDate);
}
