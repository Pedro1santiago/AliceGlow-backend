package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Tests")
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
                15
        );

        createProductDTO = new CreateProductDTO(
                "Base Líquida Glow",
                new BigDecimal("89.90"),
                20
        );

        updateProductDTO = new UpdateProductDTO(
                "Base Líquida Premium",
                new BigDecimal("99.90"),
                25
        );
    }

    @Test
    @DisplayName("Should list all products with status 200 OK")
    void shouldListProductsSuccessfully() {
        ProductDTO productDTO2 = new ProductDTO(
                2L,
                "Paleta de Sombras",
                new BigDecimal("149.90"),
                10
        );

        when(productService.listProducts())
                .thenReturn(List.of(productDTO, productDTO2));

        ResponseEntity<List<ProductDTO>> response = productController.listProducts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Base Líquida Matte", response.getBody().get(0).name());
        assertEquals("Paleta de Sombras", response.getBody().get(1).name());

        verify(productService, times(1)).listProducts();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
        when(productService.listProducts())
                .thenReturn(List.of());

        ResponseEntity<List<ProductDTO>> response = productController.listProducts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        verify(productService).listProducts();
    }

    @Test
    @DisplayName("Should create product with status 201 CREATED")
    void shouldCreateProductSuccessfully() {
        when(productService.createProduct(any(CreateProductDTO.class)))
                .thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(createProductDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Base Líquida Matte", response.getBody().name());
        assertEquals(new BigDecimal("79.90"), response.getBody().costPrice());
        assertEquals(15, response.getBody().stock());

        verify(productService, times(1)).createProduct(any(CreateProductDTO.class));
    }

    @Test
    @DisplayName("Should call productService.createProduct with correct DTO")
    void shouldCallCreateProductWithCorrectDTO() {
        when(productService.createProduct(any(CreateProductDTO.class)))
                .thenReturn(productDTO);

        productController.createProduct(createProductDTO);

        verify(productService).createProduct(any(CreateProductDTO.class));
    }

    @Test
    @DisplayName("Should update product with status 200 OK")
    void shouldUpdateProductSuccessfully() {
        Long productId = 1L;
        ProductDTO updatedProductDTO = new ProductDTO(
                productId,
                "Base Líquida Premium",
                new BigDecimal("99.90"),
                25
        );

        when(productService.updateProduct(eq(productId), any(UpdateProductDTO.class)))
                .thenReturn(updatedProductDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, updateProductDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Base Líquida Premium", response.getBody().name());
        assertEquals(new BigDecimal("99.90"), response.getBody().costPrice());
        assertEquals(25, response.getBody().stock());

        verify(productService, times(1)).updateProduct(eq(productId), any(UpdateProductDTO.class));
    }

    @Test
    @DisplayName("Should call productService.updateProduct with correct parameters")
    void shouldCallUpdateProductWithCorrectParameters() {
        Long productId = 1L;
        when(productService.updateProduct(eq(productId), any(UpdateProductDTO.class)))
                .thenReturn(productDTO);

        productController.updateProduct(productId, updateProductDTO);

        verify(productService).updateProduct(eq(productId), any(UpdateProductDTO.class));
    }

    @Test
    @DisplayName("Should delete product with status 204 NO CONTENT")
    void shouldDeleteProductSuccessfully() {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService, times(1)).deleteProduct(productId);
        verify(productService, never()).listProducts();
    }

    @Test
    @DisplayName("Should call productService.deleteProduct with correct ID")
    void shouldCallDeleteProductWithCorrectId() {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        productController.deleteProduct(productId);

        verify(productService).deleteProduct(eq(productId));
    }
}
