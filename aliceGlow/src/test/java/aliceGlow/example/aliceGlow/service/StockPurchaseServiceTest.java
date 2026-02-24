package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.StockPurchase;
import aliceGlow.example.aliceGlow.dto.stockPurchase.CreateStockPurchaseDTO;
import aliceGlow.example.aliceGlow.repository.StockPurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

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
            ReflectionTestUtils.setField(p, "id", 1L);
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
        ReflectionTestUtils.setField(p1, "id", 1L);
        p1.setPurchaseDate(LocalDate.of(2026, 2, 2));
        p1.setDescription("Restock");

        when(stockPurchaseRepository.findAllByPurchaseDateBetween(any(), any()))
                .thenReturn(List.of(p1));

        var result = stockPurchaseService.listByPeriod(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }

    @Test
    void shouldListStockPurchasesByPeriodPage() {
        StockPurchase p1 = new StockPurchase();
        ReflectionTestUtils.setField(p1, "id", 1L);
        p1.setPurchaseDate(LocalDate.of(2026, 2, 2));
        p1.setDescription("Restock");

        Page<StockPurchase> page = new PageImpl<>(List.of(p1), PageRequest.of(0, 20), 1);
        when(stockPurchaseRepository.findAllByPurchaseDateBetween(any(), any(), eq(PageRequest.of(0, 20))))
                .thenReturn(page);

        var result = stockPurchaseService.listByPeriodPage(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                PageRequest.of(0, 20)
        );

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).id());
    }
}
