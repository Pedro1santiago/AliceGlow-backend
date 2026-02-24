package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.cashBox.CashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.CreateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.UpdateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CreateCashOutflowDTO;
import aliceGlow.example.aliceGlow.service.CashBoxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashBoxControllerTest {

    @Mock private CashBoxService cashBoxService;

    @InjectMocks private CashBoxController cashBoxController;

    @Test
    void shouldCreateCashBox() {
        CashBoxDTO dto = new CashBoxDTO(1L, LocalDate.of(2026, 2, 1), new BigDecimal("10.00"), null, null, List.of());
        when(cashBoxService.create(any())).thenReturn(dto);

        var response = cashBoxController.create(new CreateCashBoxDTO(LocalDate.of(2026, 2, 1), new BigDecimal("10.00")));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    void shouldFindCashBoxById() {
        CashBoxDTO dto = new CashBoxDTO(1L, LocalDate.of(2026, 2, 1), new BigDecimal("10.00"), null, null, List.of());
        when(cashBoxService.findById(1L)).thenReturn(dto);

        var response = cashBoxController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    void shouldFindCashBoxByBusinessDate() {
        LocalDate date = LocalDate.of(2026, 2, 1);
        CashBoxDTO dto = new CashBoxDTO(1L, date, new BigDecimal("10.00"), null, null, List.of());
        when(cashBoxService.findByBusinessDate(date)).thenReturn(dto);

        var response = cashBoxController.findByBusinessDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(date, response.getBody().businessDate());
    }

    @Test
    void shouldListAllCashBoxes() {
        CashBoxDTO dto1 = new CashBoxDTO(1L, LocalDate.of(2026, 2, 2), new BigDecimal("20.00"), null, null, List.of());
        CashBoxDTO dto2 = new CashBoxDTO(2L, LocalDate.of(2026, 2, 1), new BigDecimal("10.00"), null, null, List.of());
        when(cashBoxService.listAll()).thenReturn(List.of(dto1, dto2));

        var response = cashBoxController.listAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).id());
    }

    @Test
    void shouldListAllCashBoxesPage() {
        CashBoxDTO dto1 = new CashBoxDTO(1L, LocalDate.of(2026, 2, 2), new BigDecimal("20.00"), null, null, List.of());
        Page<CashBoxDTO> page = new PageImpl<>(List.of(dto1), PageRequest.of(0, 20), 1);
        when(cashBoxService.listAllPage(PageRequest.of(0, 20))).thenReturn(page);

        var response = cashBoxController.listAllPage(PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1L, response.getBody().getContent().get(0).id());
    }

    @Test
    void shouldUpdateBalance() {
        CashBoxDTO dto = new CashBoxDTO(1L, LocalDate.of(2026, 2, 1), new BigDecimal("20.00"), null, null, List.of());
        when(cashBoxService.updateBalance(eq(1L), any())).thenReturn(dto);

        var response = cashBoxController.updateBalance(1L, new UpdateCashBoxDTO(new BigDecimal("20.00")));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("20.00"), response.getBody().balance());
    }

    @Test
    void shouldAddOutflow() {
        CashOutflowDTO outflowDTO = new CashOutflowDTO(
                1L,
                1L,
                "Delivery",
                new BigDecimal("5.00"),
                LocalDateTime.of(2026, 2, 1, 10, 0),
                null
        );
        when(cashBoxService.addOutflow(eq(1L), any())).thenReturn(outflowDTO);

        var response = cashBoxController.addOutflow(
                1L,
                new CreateCashOutflowDTO("Delivery", new BigDecimal("5.00"), LocalDateTime.of(2026, 2, 1, 10, 0))
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().cashBoxId());
    }
}
