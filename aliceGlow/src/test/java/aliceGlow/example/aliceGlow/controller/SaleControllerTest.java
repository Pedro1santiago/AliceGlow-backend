package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.domain.PaymentMethod;
import aliceGlow.example.aliceGlow.domain.SaleStatus;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @Mock
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    private SaleDTO saleDTO;
    private CreateSaleDTO createSaleDTO;

    @BeforeEach
    void setUp() {

        saleDTO = new SaleDTO(
                1L,
                LocalDateTime.now(),
                "Diana",
                new BigDecimal("100.00"),
                SaleStatus.PENDING,
                PaymentMethod.PIX,
                null,
                List.of()
        );

        createSaleDTO = new CreateSaleDTO(
                "Diana",
                PaymentMethod.PIX,
                List.of(new CreateSaleItemDTO(1L, 2))
        );
    }

    @Test
    void shouldListSalesSuccessfully() {

        when(saleService.listSales()).thenReturn(List.of(saleDTO));

        ResponseEntity<List<SaleDTO>> response = saleController.listSales();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(saleService).listSales();
    }

    @Test
    void shouldFindSaleByIdSuccessfully() {

        when(saleService.findById(1L)).thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = saleController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Diana", response.getBody().client());

        verify(saleService).findById(1L);
    }

    @Test
    void shouldCreateSaleSuccessfully() {

        when(saleService.sale(any())).thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = saleController.create(createSaleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Diana", response.getBody().client());

        verify(saleService).sale(any());
    }

    @Test
    void shouldCancelSaleSuccessfully() {

        ResponseEntity<Void> response = saleController.cancel(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(saleService).cancelSale(1L);
    }

    @Test
    void shouldPaySaleSuccessfully() {

        ResponseEntity<Void> response = saleController.pay(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(saleService).markAsPaid(1L);
    }
}