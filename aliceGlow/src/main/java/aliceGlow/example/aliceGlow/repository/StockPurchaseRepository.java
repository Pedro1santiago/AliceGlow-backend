package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.StockPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockPurchaseRepository extends JpaRepository<StockPurchase, Long> {

    @Query("""
        SELECT p
        FROM StockPurchase p
        WHERE p.purchaseDate BETWEEN :start AND :end
        ORDER BY p.purchaseDate DESC
    """)
    List<StockPurchase> findAllByPurchaseDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
        SELECT p
        FROM StockPurchase p
        WHERE p.purchaseDate BETWEEN :start AND :end
        ORDER BY p.purchaseDate DESC
    """)
    Page<StockPurchase> findAllByPurchaseDateBetween(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            Pageable pageable
    );
}
