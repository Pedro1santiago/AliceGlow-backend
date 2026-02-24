package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.StockPurchase;
import aliceGlow.example.aliceGlow.dto.stockPurchase.CreateStockPurchaseDTO;
import aliceGlow.example.aliceGlow.repository.StockPurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockPurchaseServiceTest {

    @Mock private StockPurchaseRepository stockPurchaseRepository;

    @InjectMocks private StockPurchaseService stockPurchaseService;

    @Test
    void shouldCreateStockPurchase() {
        when(stockPurchaseRepository.save(any())).thenAnswer(i -> {
            StockPurchase p = i.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });

        var dto = new CreateStockPurchaseDTO(LocalDate.of(2026, 2, 2), "Weekly restock");
        var result = stockPurchaseService.create(dto);

        assertEquals(1L, result.id());
        assertEquals(LocalDate.of(2026, 2, 2), result.purchaseDate());
        assertEquals("Weekly restock", result.description());
    }

    @Test
    void shouldListStockPurchasesByPeriod() {
        StockPurchase p1 = new StockPurchase();
        p1.setId(1L);
        p1.setPurchaseDate(LocalDate.of(2026, 2, 2));
        p1.setDescription("Restock");

        when(stockPurchaseRepository.findAllByPurchaseDateBetween(any(), any()))
                .thenReturn(List.of(p1));

        var result = stockPurchaseService.listByPeriod(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }
}
