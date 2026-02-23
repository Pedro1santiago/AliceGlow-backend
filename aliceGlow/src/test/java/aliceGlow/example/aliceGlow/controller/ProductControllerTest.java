package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO productDTO;
    private CreateProductDTO createProductDTO;
    private UpdateProductDTO updateProductDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(
                1L,
                "Base Líquida Matte",
                new BigDecimal("79.90"),
            new BigDecimal("129.90"),
                15
        );

        createProductDTO = new CreateProductDTO(
                "Base Líquida Glow",
                new BigDecimal("89.90"),
            new BigDecimal("139.90"),
                20
        );

        updateProductDTO = new UpdateProductDTO(
                "Base Líquida Premium",
                new BigDecimal("99.90"),
            new BigDecimal("149.90"),
                25
        );
    }

    @Test
    void shouldListProductsSuccessfully() {
        when(productService.listProducts()).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductDTO>> response = productController.listProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService).listProducts();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(productService.createProduct(any())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(createProductDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Base Líquida Matte", response.getBody().name());
        verify(productService).createProduct(any());
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        when(productService.updateProduct(eq(1L), any())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response =
                productController.updateProduct(1L, updateProductDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).updateProduct(eq(1L), any());
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }
}
