package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SaleController Tests")
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
                List.of()
        );

        createSaleDTO = new CreateSaleDTO(
                "Diana",
                List.of(new CreateSaleItemDTO(1L, 2))
        );
    }

    @Test
    @DisplayName("Should list all sales with status 200 OK")
    void shouldListSalesSuccessfully() {
        SaleDTO saleDTO2 = new SaleDTO(
                2L,
                LocalDateTime.now(),
                "Ana",
                new BigDecimal("250.00"),
                List.of()
        );

        when(saleService.listSales())
                .thenReturn(List.of(saleDTO, saleDTO2));

        ResponseEntity<List<SaleDTO>> response = saleController.listSales();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Diana", response.getBody().get(0).client());
        assertEquals("Ana", response.getBody().get(1).client());

        verify(saleService, times(1)).listSales();
    }

    @Test
    @DisplayName("Should return empty list when no sales exist")
    void shouldReturnEmptyListWhenNoSales() {
        when(saleService.listSales())
                .thenReturn(List.of());

        ResponseEntity<List<SaleDTO>> response = saleController.listSales();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        verify(saleService).listSales();
    }

    @Test
    @DisplayName("Should find sale by ID with status 200 OK")
    void shouldFindSaleByIdSuccessfully() {
        Long saleId = 1L;

        when(saleService.findById(saleId))
                .thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = saleController.findById(saleId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Diana", response.getBody().client());
        assertEquals(new BigDecimal("100.00"), response.getBody().total());

        verify(saleService, times(1)).findById(saleId);
    }

    @Test
    @DisplayName("Should call saleService.findById with correct ID")
    void shouldCallFindByIdWithCorrectId() {
        Long saleId = 1L;
        when(saleService.findById(saleId))
                .thenReturn(saleDTO);

        saleController.findById(saleId);

        verify(saleService).findById(eq(saleId));
    }

    @Test
    @DisplayName("Should create sale with status 201 CREATED")
    void shouldCreateSaleSuccessfully() {
        when(saleService.sale(any(CreateSaleDTO.class)))
                .thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = saleController.create(createSaleDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Diana", response.getBody().client());
        assertEquals(new BigDecimal("100.00"), response.getBody().total());

        verify(saleService, times(1)).sale(any(CreateSaleDTO.class));
    }

    @Test
    @DisplayName("Should call saleService.sale with correct DTO")
    void shouldCallSaleWithCorrectDTO() {
        when(saleService.sale(any(CreateSaleDTO.class)))
                .thenReturn(saleDTO);

        saleController.create(createSaleDTO);

        verify(saleService).sale(any(CreateSaleDTO.class));
    }

    @Test
    @DisplayName("Should create sale with correct client and total")
    void shouldCreateSaleWithCorrectData() {
        when(saleService.sale(any(CreateSaleDTO.class)))
                .thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = saleController.create(createSaleDTO);

        assertEquals("Diana", response.getBody().client());
        assertEquals(new BigDecimal("100.00"), response.getBody().total());
    }

    @Test
    @DisplayName("Should cancel sale with status 204 NO CONTENT")
    void shouldCancelSaleSuccessfully() {
        Long saleId = 1L;
        doNothing().when(saleService).cancelSale(saleId);

        ResponseEntity<Void> response = saleController.cancel(saleId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(saleService, times(1)).cancelSale(saleId);
        verify(saleService, never()).listSales();
    }

    @Test
    @DisplayName("Should call saleService.cancelSale with correct ID")
    void shouldCallCancelSaleWithCorrectId() {
        Long saleId = 1L;
        doNothing().when(saleService).cancelSale(saleId);

        saleController.cancel(saleId);

        verify(saleService).cancelSale(eq(saleId));
    }

    @Test
    @DisplayName("Should cancel sale and return no content")
    void shouldCancelSaleAndReturnNoContent() {
        Long saleId = 1L;
        doNothing().when(saleService).cancelSale(saleId);

        ResponseEntity<Void> response = saleController.cancel(saleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
