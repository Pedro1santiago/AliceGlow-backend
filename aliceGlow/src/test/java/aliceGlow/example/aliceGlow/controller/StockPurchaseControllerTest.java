package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.stockPurchase.StockPurchaseDTO;
import aliceGlow.example.aliceGlow.service.StockPurchaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockPurchaseControllerTest {

    @Mock private StockPurchaseService stockPurchaseService;

    @InjectMocks private StockPurchaseController stockPurchaseController;

    @Test
    void shouldListByStartEnd() {
        when(stockPurchaseService.listByPeriod(eq(LocalDate.of(2026, 2, 1)), eq(LocalDate.of(2026, 2, 28))))
                .thenReturn(List.of(new StockPurchaseDTO(1L, LocalDate.of(2026, 2, 2), "X", null)));

        var response = stockPurchaseController.list(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                null,
                null
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldListByMonthYear() {
        when(stockPurchaseService.listByPeriod(eq(LocalDate.of(2026, 2, 1)), eq(LocalDate.of(2026, 2, 28))))
                .thenReturn(List.of());

        var response = stockPurchaseController.list(null, null, 2, 2026);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
