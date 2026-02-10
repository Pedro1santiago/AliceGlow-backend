package aliceGlow.example.aliceGlow.service;
import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.domain.Sale;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SaleService saleService;

    @Test
    void shouldReturnAllSales() {

        Sale saleOne = new Sale();
        saleOne.setClient("Daniele");
        saleOne.setTotal(new BigDecimal("20.00"));
        saleOne.setItems(new ArrayList<>());

        Sale saleTwo = new Sale();
        saleTwo.setClient("Marcia");
        saleTwo.setTotal(new BigDecimal("158.90"));
        saleTwo.setItems(new ArrayList<>());

        when(saleRepository.findAll())
                .thenReturn(List.of(saleOne, saleTwo));

        List<SaleDTO> result = saleService.listSales();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Daniele", result.get(0).client());
        assertEquals(new BigDecimal("20.00"), result.get(0).total());

        assertEquals("Marcia", result.get(1).client());
        assertEquals(new BigDecimal("158.90"), result.get(1).total());

        verify(saleRepository).findAll();
    }

    @Test
    void shouldCreateSale() {

        CreateSaleDTO saleDTO = new CreateSaleDTO(
                "Diana",
                new ArrayList<>()
        );

        Sale sale = new Sale();
        sale.setClient("Diana");
        sale.setItems(new ArrayList<>());
        sale.setTotal(new BigDecimal("100.00"));

        when(saleRepository.save(any(Sale.class)))
                .thenReturn(sale);

        SaleDTO result = saleService.sale(saleDTO);

        assertNotNull(result);
        assertEquals("Diana", result.client());
        assertEquals(new ArrayList<>(), result.saleItems());
        assertEquals(new BigDecimal("100.00"), result.total());
    }

    @Test
    void shouldDeleteSaleById() {

        Long saleId = 1L;

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setClient("Diana");
        sale.setItems(new ArrayList<>());
        sale.setTotal(new BigDecimal("10.00"));

        when(saleRepository.findById(saleId))
                .thenReturn(Optional.of(sale));

        saleService.deleteSale(saleId);

        verify(saleRepository).findById(saleId);
        verify(saleRepository).delete(sale);
    }

    @Test
    void shouldCreateSaleWithItemsSuccessfully() {

        Long productIdOne = 1L;
        Long productIdTwo = 2L;

        Product productOne = new Product();
        productOne.setId(productIdOne);
        productOne.setName("Base");
        productOne.setCostPrice(new BigDecimal("50.00"));

        Product productTwo = new Product();
        productTwo.setId(productIdTwo);
        productTwo.setName("Base Líquida");
        productTwo.setCostPrice(new BigDecimal("60.00"));

        CreateSaleItemDTO itemOne = new CreateSaleItemDTO(productIdOne, 10);
        CreateSaleItemDTO itemTwo = new CreateSaleItemDTO(productIdTwo, 10);

        CreateSaleDTO dto = new CreateSaleDTO(
                "Diana",
                List.of(itemOne, itemTwo)
        );

        when(productRepository.findById(productIdOne))
                .thenReturn(Optional.of(productOne));
        when(productRepository.findById(productIdTwo))
                .thenReturn(Optional.of(productTwo));
        when(saleRepository.save(any(Sale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SaleDTO result = saleService.sale(dto);

        assertNotNull(result);
        assertEquals("Diana", result.client());
        assertEquals(2, result.saleItems().size());
        assertEquals(new BigDecimal("1100.00"), result.total());

        verify(productRepository).findById(productIdOne);
        verify(productRepository).findById(productIdTwo);
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void shouldThrowExceptionWhenSaleNotFoundOnDelete(){

        Long saleId = 1L;

        when(saleRepository.findById(saleId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> saleService.deleteSale(saleId));

        assertEquals("Sale not found", exception.getMessage());

        verify(saleRepository).findById(saleId);

    }

}
