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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
                , true
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
        when(productService.listProducts(null, false)).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductDTO>> response = productController.listProducts(null, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService).listProducts(null, false);
    }

    @Test
    void shouldListProductsPageSuccessfully() {
        Page<ProductDTO> page = new PageImpl<>(List.of(productDTO), PageRequest.of(0, 20), 1);
        when(productService.listProductsPage(null, false, PageRequest.of(0, 20))).thenReturn(page);

        ResponseEntity<Page<ProductDTO>> response = productController.listProductsPage(null, false, PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(productService).listProductsPage(null, false, PageRequest.of(0, 20));
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

    @Test
    void shouldActivateProductSuccessfully() {
        when(productService.activateProduct(1L)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.activateProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().active());
        verify(productService).activateProduct(1L);
    }

    @Test
    void shouldDeactivateProductSuccessfully() {
        ProductDTO inactive = new ProductDTO(
                1L,
                "Base Líquida Matte",
                new BigDecimal("79.90"),
                new BigDecimal("129.90"),
                15,
                false
        );

        when(productService.deactivateProduct(1L)).thenReturn(inactive);

        ResponseEntity<ProductDTO> response = productController.deactivateProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().active());
        verify(productService).deactivateProduct(1L);
    }
}
