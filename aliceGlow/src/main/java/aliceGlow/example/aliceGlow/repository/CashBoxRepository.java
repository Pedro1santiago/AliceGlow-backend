package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.CashBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
    boolean existsByBusinessDate(LocalDate businessDate);
    Optional<CashBox> findByBusinessDate(LocalDate businessDate);

    List<CashBox> findAllByOrderByBusinessDateDesc();

    Page<CashBox> findAllByOrderByBusinessDateDesc(Pageable pageable);
}
