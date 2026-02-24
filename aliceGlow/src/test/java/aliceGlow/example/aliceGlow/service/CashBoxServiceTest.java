package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.CashBox;
import aliceGlow.example.aliceGlow.domain.CashOutflow;
import aliceGlow.example.aliceGlow.dto.cashBox.CreateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CreateCashOutflowDTO;
import aliceGlow.example.aliceGlow.exception.CashBoxAlreadyExistsForDateException;
import aliceGlow.example.aliceGlow.exception.CashBoxNotFoundException;
import aliceGlow.example.aliceGlow.repository.CashBoxRepository;
import aliceGlow.example.aliceGlow.repository.CashOutflowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashBoxServiceTest {

    @Mock private CashBoxRepository cashBoxRepository;
    @Mock private CashOutflowRepository cashOutflowRepository;

    @InjectMocks private CashBoxService cashBoxService;

    @Test
    void shouldCreateCashBox() {
        LocalDate businessDate = LocalDate.of(2026, 2, 1);

        when(cashBoxRepository.existsByBusinessDate(businessDate)).thenReturn(false);
        when(cashBoxRepository.save(any())).thenAnswer(i -> {
            CashBox cb = i.getArgument(0);
            cb.setId(1L);
            cb.setCreatedAt(LocalDateTime.now());
            cb.setUpdatedAt(LocalDateTime.now());
            return cb;
        });

        var dto = new CreateCashBoxDTO(businessDate, new BigDecimal("100.00"));
        var result = cashBoxService.create(dto);

        assertEquals(1L, result.id());
        assertEquals(businessDate, result.businessDate());
        assertEquals(new BigDecimal("100.00"), result.balance());
    }

    @Test
    void shouldThrowWhenCashBoxAlreadyExistsForDate() {
        LocalDate businessDate = LocalDate.of(2026, 2, 1);
        when(cashBoxRepository.existsByBusinessDate(businessDate)).thenReturn(true);

        var dto = new CreateCashBoxDTO(businessDate, new BigDecimal("100.00"));
        assertThrows(CashBoxAlreadyExistsForDateException.class, () -> cashBoxService.create(dto));
        verify(cashBoxRepository, never()).save(any());
    }

    @Test
    void shouldAddOutflowAndDecreaseBalance() {
        CashBox cashBox = new CashBox();
        cashBox.setId(10L);
        cashBox.setBalance(new BigDecimal("200.00"));

        when(cashBoxRepository.findById(10L)).thenReturn(Optional.of(cashBox));
        when(cashBoxRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(cashOutflowRepository.save(any())).thenAnswer(i -> {
            CashOutflow outflow = i.getArgument(0);
            outflow.setId(99L);
            outflow.setCreatedAt(LocalDateTime.now());
            return outflow;
        });

        var dto = new CreateCashOutflowDTO(
                "Delivery",
                new BigDecimal("25.50"),
                LocalDateTime.of(2026, 2, 1, 12, 0)
        );

        var result = cashBoxService.addOutflow(10L, dto);

        assertEquals(99L, result.id());
        assertEquals(10L, result.cashBoxId());
        assertEquals(new BigDecimal("25.50"), result.amount());
        assertEquals(new BigDecimal("174.50"), cashBox.getBalance());
    }

    @Test
    void shouldThrowWhenCashBoxNotFoundOnAddOutflow() {
        when(cashBoxRepository.findById(10L)).thenReturn(Optional.empty());
        var dto = new CreateCashOutflowDTO(
                "Delivery",
                new BigDecimal("25.50"),
                LocalDateTime.of(2026, 2, 1, 12, 0)
        );
        assertThrows(CashBoxNotFoundException.class, () -> cashBoxService.addOutflow(10L, dto));
    }
}
