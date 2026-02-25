package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @EntityGraph(attributePaths = "items")
    @Query("SELECT s FROM Sale s")
    List<Sale> findAllWithItems();

    @EntityGraph(attributePaths = "items")
    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    List<Sale> findAllByCreatedAtBetweenWithItems(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @EntityGraph(attributePaths = "items")
    @Query("SELECT s FROM Sale s WHERE s.id = :id")
    Optional<Sale> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.status <> 'CANCELED'")
    BigDecimal sumAllInvoicing();

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.status <> 'CANCELED' AND s.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumInvoicingByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Sale> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Sale> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
