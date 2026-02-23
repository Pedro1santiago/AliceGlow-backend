package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.*;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.exception.ProductNotFoundException;
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
                PaymentMethod.PIX,
                List.of(new CreateSaleItemDTO(productId, 2))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(saleRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SaleDTO result = saleService.sale(dto);

        assertEquals("Diana", result.client());
        assertEquals(new BigDecimal("100.00"), result.total());
        assertEquals(SaleStatus.PENDING, result.status());
    }

    @Test
    void shouldCancelSale() {

        Long id = 1L;
        Sale sale = new Sale();
        sale.setStatus(SaleStatus.PENDING);

        when(saleRepository.findById(id)).thenReturn(Optional.of(sale));

        saleService.cancelSale(id);

        assertEquals(SaleStatus.CANCELED, sale.getStatus());
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
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SaleNotFoundException.class, () -> saleService.cancelSale(1L));
    }

    @Test
    void shouldThrowWhenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
                PaymentMethod.PIX,
                List.of(new CreateSaleItemDTO(1L, 2))
        );

        assertThrows(ProductNotFoundException.class,
                () -> saleService.sale(dto));
    }

    @Test
    void shouldThrowWhenSaleHasNoItems() {

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
                PaymentMethod.PIX,
                List.of()
        );

        assertThrows(SaleWithoutItemsException.class,
                () -> saleService.sale(dto));
    }
}