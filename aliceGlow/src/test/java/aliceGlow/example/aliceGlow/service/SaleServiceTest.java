package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.*;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.UpdateSaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.exception.ProductNotFoundException;
import aliceGlow.example.aliceGlow.exception.SaleCannotBeEditedException;
import aliceGlow.example.aliceGlow.exception.SaleNotFoundException;
import aliceGlow.example.aliceGlow.exception.SaleWithoutItemsException;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleItemRepository;
import aliceGlow.example.aliceGlow.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock private SaleRepository saleRepository;
    @Mock private ProductRepository productRepository;
    @Mock private SaleItemRepository saleItemRepository;

    @InjectMocks private SaleService saleService;

    @Test
    void shouldCreateSaleSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setCostPrice(new BigDecimal("50.00"));
        product.setStock(10);

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
                null,
                PaymentMethod.PIX,
            List.of(new CreateSaleItemDTO(productId, 2, new BigDecimal("80.00")))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(saleRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SaleDTO result = saleService.sale(dto);

        assertEquals("Diana", result.client());
        assertEquals(new BigDecimal("160.00"), result.total());
        assertEquals(new BigDecimal("100.00"), result.costTotal());
        assertEquals(new BigDecimal("60.00"), result.profit());
        assertEquals(SaleStatus.PENDING, result.status());
    }

    @Test
    void shouldCancelSale() {

        Long id = 1L;

        Product product = new Product();
        product.setId(10L);
        product.setStock(5);
        product.setCostPrice(new BigDecimal("10.00"));

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.PENDING);

        SaleItem item = new SaleItem();
        item.setSale(sale);
        item.setProduct(product);
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("20.00"));
        item.setUnitCostPrice(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("60.00"));
        item.setCostSubtotal(new BigDecimal("30.00"));

        sale.setItems(List.of(item));

        when(saleRepository.findByIdWithItems(id)).thenReturn(Optional.of(sale));

        saleService.cancelSale(id);

        assertEquals(SaleStatus.CANCELED, sale.getStatus());
        assertEquals(8, product.getStock());
        verify(saleRepository).save(sale);
    }

    @Test
    void shouldMarkSaleAsPaid() {

        Long id = 1L;
        Sale sale = new Sale();
        sale.setStatus(SaleStatus.PENDING);

        when(saleRepository.findById(id)).thenReturn(Optional.of(sale));

        saleService.markAsPaid(id);

        assertEquals(SaleStatus.PAID, sale.getStatus());
        assertNotNull(sale.getPaidAt());
        verify(saleRepository).save(sale);
    }

    @Test
    void shouldThrowWhenSaleNotFound() {
        when(saleRepository.findByIdWithItems(1L)).thenReturn(Optional.empty());
        assertThrows(SaleNotFoundException.class, () -> saleService.cancelSale(1L));
    }

    @Test
    void shouldThrowWhenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
                null,
                PaymentMethod.PIX,
            List.of(new CreateSaleItemDTO(1L, 2, new BigDecimal("80.00")))
        );

        assertThrows(ProductNotFoundException.class,
                () -> saleService.sale(dto));
    }

    @Test
    void shouldThrowWhenSaleHasNoItems() {

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
            null,
                PaymentMethod.PIX,
                List.of()
        );

        assertThrows(SaleWithoutItemsException.class,
                () -> saleService.sale(dto));
    }

    @Test
    void shouldReturnProfitByPeriod() {
        when(saleItemRepository.calculateTotalProfitByPeriod(any(), any()))
                .thenReturn(new BigDecimal("123.45"));

        BigDecimal result = saleService.profitByPeriod(
                java.time.LocalDateTime.of(2026, 2, 1, 0, 0),
                java.time.LocalDateTime.of(2026, 2, 28, 23, 59)
        );

        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void shouldUpdateSaleAndAdjustStockByDelta() {
        Long saleId = 1L;

        Product product = new Product();
        product.setId(10L);
        product.setStock(10);
        product.setCostPrice(new BigDecimal("5.00"));

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.PENDING);
        sale.setClient("Old");
        sale.setPaymentMethod(PaymentMethod.PIX);
        sale.setCreatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));

        SaleItem oldItem = new SaleItem();
        oldItem.setSale(sale);
        oldItem.setProduct(product);
        oldItem.setQuantity(2);
        oldItem.setUnitPrice(new BigDecimal("10.00"));
        oldItem.setUnitCostPrice(new BigDecimal("5.00"));
        oldItem.setSubtotal(new BigDecimal("20.00"));
        oldItem.setCostSubtotal(new BigDecimal("10.00"));
        sale.setItems(List.of(oldItem));

        // New quantity goes from 2 -> 5, delta +3 => product stock decrements by 3
        UpdateSaleDTO dto = new UpdateSaleDTO(
                "New Client",
                LocalDateTime.of(2026, 3, 2, 9, 0),
            PaymentMethod.CREDIT_CARD,
                List.of(new CreateSaleItemDTO(product.getId(), 5, new BigDecimal("12.00")))
        );

        when(saleRepository.findByIdWithItems(saleId)).thenReturn(Optional.of(sale));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(saleRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SaleDTO updated = saleService.updateSale(saleId, dto);

        assertEquals("New Client", updated.client());
        assertEquals(PaymentMethod.CREDIT_CARD, updated.paymentMethod());
        assertEquals(LocalDateTime.of(2026, 3, 2, 9, 0), updated.createdAt());
        assertEquals(7, product.getStock());
        assertEquals(new BigDecimal("60.00"), updated.total());
    }

    @Test
    void shouldNotAllowEditingCanceledSale() {
        Long saleId = 1L;
        Sale sale = new Sale();
        sale.setStatus(SaleStatus.CANCELED);

        when(saleRepository.findByIdWithItems(saleId)).thenReturn(Optional.of(sale));

        UpdateSaleDTO dto = new UpdateSaleDTO(
                "Client",
                null,
                PaymentMethod.PIX,
                List.of(new CreateSaleItemDTO(1L, 1, new BigDecimal("10.00")))
        );

        assertThrows(SaleCannotBeEditedException.class, () -> saleService.updateSale(saleId, dto));
    }
}