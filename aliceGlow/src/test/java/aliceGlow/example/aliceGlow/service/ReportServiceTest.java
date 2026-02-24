package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.CashBox;
import aliceGlow.example.aliceGlow.domain.CashOutflow;
import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.repository.CashOutflowRepository;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private SaleItemRepository saleItemRepository;
    @Mock private CashOutflowRepository cashOutflowRepository;

    @InjectMocks private ReportService reportService;

    @Test
    void shouldReturnProductsSoldStatus() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("A");
        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("B");

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));
        when(saleItemRepository.sumSoldQuantityByProductIdBetween(any(), any()))
                .thenReturn(List.<Object[]>of(new Object[]{1L, 3L}));

        var result = reportService.productsSoldStatus(
                LocalDateTime.of(2026, 2, 1, 0, 0),
                LocalDateTime.of(2026, 2, 28, 23, 59)
        );

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.productId().equals(1L) && r.sold() && r.soldQuantity() == 3L));
        assertTrue(result.stream().anyMatch(r -> r.productId().equals(2L) && !r.sold() && r.soldQuantity() == 0L));
    }

    @Test
    void shouldReturnCashOutflowsReportWithZeroWhenNoSum() {
        CashBox cb = new CashBox();
                ReflectionTestUtils.setField(cb, "id", 10L);

        CashOutflow outflow = new CashOutflow();
        ReflectionTestUtils.setField(outflow, "id", 1L);
        outflow.setCashBox(cb);
        outflow.setDescription("X");
        outflow.setAmount(new BigDecimal("5.00"));
        outflow.setOccurredAt(LocalDateTime.of(2026, 2, 1, 10, 0));
        ReflectionTestUtils.setField(outflow, "createdAt", LocalDateTime.of(2026, 2, 1, 10, 5));

        when(cashOutflowRepository.findAllByOccurredAtBetween(any(), any()))
                .thenReturn(List.of(outflow));
        when(cashOutflowRepository.sumAmountByOccurredAtBetween(any(), any()))
                .thenReturn(null);

        var report = reportService.cashOutflows(
                LocalDateTime.of(2026, 2, 1, 0, 0),
                LocalDateTime.of(2026, 2, 1, 23, 59)
        );

        assertEquals(BigDecimal.ZERO, report.total());
        assertEquals(1, report.outflows().size());
        assertEquals(10L, report.outflows().get(0).cashBoxId());
    }

        @Test
        void shouldReturnProductsSoldStatusSummary() {
                Product p1 = new Product();
                p1.setId(1L);
                p1.setName("A");
                Product p2 = new Product();
                p2.setId(2L);
                p2.setName("B");
                Product p3 = new Product();
                p3.setId(3L);
                p3.setName("C");

                when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));
                when(saleItemRepository.sumSoldQuantityByProductIdBetween(any(), any()))
                                .thenReturn(List.<Object[]>of(
                                                new Object[]{1L, 1L},
                                                new Object[]{3L, 5L}
                                ));

                var summary = reportService.productsSoldStatusSummary(
                                LocalDateTime.of(2026, 2, 1, 0, 0),
                                LocalDateTime.of(2026, 2, 28, 23, 59)
                );

                assertEquals(2L, summary.soldProducts());
                assertEquals(1L, summary.notSoldProducts());
        }
}
