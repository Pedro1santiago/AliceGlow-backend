package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.status <> 'CANCELED'")
    BigDecimal sumAllInvoicing();

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.status <> 'CANCELED' AND s.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumInvoicingByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
