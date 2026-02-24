package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowReportDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductSoldStatusDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
import aliceGlow.example.aliceGlow.service.ReportService;
import aliceGlow.example.aliceGlow.service.SaleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock private SaleService saleService;
    @Mock private ProductService productService;
    @Mock private ReportService reportService;

    @InjectMocks private ReportController reportController;

    @Test
    void shouldReturnProductsSalesStatus() {
        when(reportService.productsSoldStatus(any(), any()))
                .thenReturn(List.of(new ProductSoldStatusDTO(1L, "A", 1L, true)));

        LocalDateTime start = LocalDateTime.of(2026, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 28, 23, 59);

        var response = reportController.productsSalesStatus(start, end, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnCashOutflowsReport() {
        when(reportService.cashOutflows(any(), any()))
                .thenReturn(new CashOutflowReportDTO(BigDecimal.ZERO, List.of()));

        var response = reportController.cashOutflows(
                LocalDateTime.of(2026, 2, 1, 0, 0),
                LocalDateTime.of(2026, 2, 1, 23, 59),
                null,
                null
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.ZERO, response.getBody().total());
    }
}
