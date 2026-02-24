package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    @Query("""
    SELECT COALESCE(
        SUM(item.subtotal - item.costSubtotal), 0
    )
    FROM SaleItem item
    JOIN item.sale s
    WHERE s.status <> 'CANCELED'
    """)
    BigDecimal calculateTotalProfit();

    @Query("""
    SELECT p.name, SUM(si.quantity) 
    FROM SaleItem si
    JOIN si.product p
    JOIN si.sale s
    WHERE s.status <> 'CANCELED'
    GROUP BY p.name
    ORDER BY SUM(si.quantity) DESC
    """)
    List<Object[]> findTopSellingProducts();

    @Query("""
        SELECT p.id, SUM(si.quantity)
        FROM SaleItem si
        JOIN si.product p
        JOIN si.sale s
        WHERE s.status <> 'CANCELED'
          AND s.createdAt BETWEEN :start AND :end
        GROUP BY p.id
    """)
    List<Object[]> sumSoldQuantityByProductIdBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
