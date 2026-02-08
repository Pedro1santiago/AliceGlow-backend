package aliceGlow.example.aliceGlow.service;
import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService  productService;

    @Test
    void shouldListProducts(){

        Product productOne = new Product();

        productOne.setName("Base Líquida Matte");
        productOne.setCostPrice(new BigDecimal("79.70"));
        productOne.setStock(15);

        Product productTwo = new Product();

        productTwo.setName("Base Líquida Glow");
        productTwo.setCostPrice(new BigDecimal("89.90"));
        productTwo.setStock(30);

        when(productRepository.findAll())
                .thenReturn(List.of(productOne, productTwo));


        List<ProductDTO> result = productService.listProducts();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Base Líquida Matte", result.get(0).name());
        assertEquals(new BigDecimal("79.70"), result.get(0).costPrice());
        assertEquals(15, result.get(0).stock());

        assertEquals("Base Líquida Glow", result.get(1).name());
        assertEquals(new BigDecimal("89.90"), result.get(1).costPrice());
        assertEquals(30, result.get(1).stock());

        verify(productRepository).findAll();
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldCreateProductSuccessfully() {
        CreateProductDTO dto = new CreateProductDTO(
                "Base Líquida Matte",
                new BigDecimal("79.90"),
                15
        );

        Product productSaved = new Product();
        productSaved.setId(1L);
        productSaved.setName("Base Líquida Matte");
        productSaved.setCostPrice(new BigDecimal("79.90"));
        productSaved.setStock(15);

        when(productRepository.save(any(Product.class)))
                .thenReturn(productSaved);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Base Líquida Matte", result.name());
        assertEquals(new BigDecimal("79.90"), result.costPrice());
        assertEquals(15, result.stock());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        Long productId = 1L;

        UpdateProductDTO dto = new UpdateProductDTO(
                "Base Líquida Glow",
                new BigDecimal("89.90"),
                30
        );

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Base Líquida Matte");
        existingProduct.setCostPrice(new BigDecimal("79.90"));
        existingProduct.setStock(15);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Base Líquida Glow");
        updatedProduct.setCostPrice(new BigDecimal("89.90"));
        updatedProduct.setStock(30);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(productId, dto);

        assertNotNull(result);
        assertEquals("Base Líquida Glow", result.name());
        assertEquals(new BigDecimal("89.90"), result.costPrice());
        assertEquals(30, result.stock());

        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Paleta de Sombras Nude");
        product.setCostPrice(new BigDecimal("149.90"));
        product.setStock(20);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdate(){

        Long productId = 1L;

        UpdateProductDTO updateProduct = new UpdateProductDTO(
                "Base Líquida Matte",
                new BigDecimal("80.00"),
                20
        );
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());



        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.updateProduct(productId, updateProduct)
        );

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCostPriceIsNegativeOnUpdate(){

        Long productId = 1L;

        UpdateProductDTO product = new UpdateProductDTO(
                "Mouse Gamer",
                new BigDecimal("-120.00"),
                15
        );

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Mouse Gamer");
        existingProduct.setCostPrice(new BigDecimal("100.00"));
        existingProduct.setStock(10);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                        () -> productService.updateProduct(productId, product)
        );

        assertEquals("CostPrice cannot be negative", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnDelete(){

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.deleteProduct(productId)
        );

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

}