package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService  productService;

    @Test
    void shouldCreateProductSuccessfully(){
        CreateProductDTO dto = new CreateProductDTO(
                "Mouser Gamer",
                new BigDecimal("120.00"),
                10);

        Product productSaved = new Product();

        productSaved.setId(1L);
        productSaved.setName("Mouser Gamer");
        productSaved.setCostPrice(new BigDecimal("120.00"));
        productSaved.setStock(10);

        when(productRepository.save(any(Product.class)))
                .thenReturn(productSaved);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Mouser Gamer", result.name());
        assertEquals(new BigDecimal("120.00"), result.costPrice());
        assertEquals(10, result.stock());

        verify(productRepository).save(any(Product.class));

    }





}