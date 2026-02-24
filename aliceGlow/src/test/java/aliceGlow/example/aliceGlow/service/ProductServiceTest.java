package aliceGlow.example.aliceGlow.service;
import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.exception.CostPriceCannotBeNegativeException;
import aliceGlow.example.aliceGlow.exception.ProductInUseException;
import aliceGlow.example.aliceGlow.exception.ProductNotFoundException;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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


        List<ProductDTO> result = productService.listProducts(null, true);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Base Líquida Matte", result.get(0).name());
        assertEquals(new BigDecimal("79.70"), result.get(0).costPrice());
        assertEquals(15, result.get(0).stock());
        assertTrue(result.get(0).active());

        assertEquals("Base Líquida Glow", result.get(1).name());
        assertEquals(new BigDecimal("89.90"), result.get(1).costPrice());
        assertEquals(30, result.get(1).stock());
        assertTrue(result.get(1).active());

        verify(productRepository).findAll();
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldCreateProductSuccessfully() {
        CreateProductDTO dto = new CreateProductDTO(
                "Base Líquida Matte",
                new BigDecimal("79.90"),
                new BigDecimal("129.90"),
                15
        );

        Product productSaved = new Product();
        productSaved.setId(1L);
        productSaved.setName("Base Líquida Matte");
        productSaved.setCostPrice(new BigDecimal("79.90"));
        productSaved.setSalePrice(new BigDecimal("129.90"));
        productSaved.setStock(15);

        when(productRepository.save(any(Product.class)))
                .thenReturn(productSaved);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Base Líquida Matte", result.name());
        assertEquals(new BigDecimal("79.90"), result.costPrice());
        assertEquals(new BigDecimal("129.90"), result.salePrice());
        assertEquals(15, result.stock());
        assertTrue(result.active());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        Long productId = 1L;

        UpdateProductDTO dto = new UpdateProductDTO(
                "Base Líquida Glow",
                new BigDecimal("89.90"),
                new BigDecimal("139.90"),
                30
        );

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Base Líquida Matte");
        existingProduct.setCostPrice(new BigDecimal("79.90"));
        existingProduct.setSalePrice(new BigDecimal("129.90"));
        existingProduct.setStock(15);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Base Líquida Glow");
        updatedProduct.setCostPrice(new BigDecimal("89.90"));
        updatedProduct.setSalePrice(new BigDecimal("139.90"));
        updatedProduct.setStock(30);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(productId, dto);

        assertNotNull(result);
        assertEquals("Base Líquida Glow", result.name());
        assertEquals(new BigDecimal("89.90"), result.costPrice());
        assertEquals(new BigDecimal("139.90"), result.salePrice());
        assertEquals(30, result.stock());
        assertTrue(result.active());

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
    void shouldListOnlyActiveByDefaultWhenActiveIsNullAndIncludeInactiveFalse() {

        Product activeProduct = new Product();
        activeProduct.setName("Ativo");
        activeProduct.setCostPrice(new BigDecimal("10.00"));
        activeProduct.setSalePrice(new BigDecimal("20.00"));
        activeProduct.setStock(1);
        activeProduct.setActive(true);

        when(productRepository.findAllByActiveTrue())
                .thenReturn(List.of(activeProduct));

        List<ProductDTO> result = productService.listProducts(null, false);

        assertEquals(1, result.size());
        assertTrue(result.get(0).active());
        verify(productRepository).findAllByActiveTrue();
        verify(productRepository, never()).findAllByActive(anyBoolean());
        verify(productRepository, never()).findAll();
    }

    @Test
    void shouldListInactiveWhenActiveFalse() {

        Product inactiveProduct = new Product();
        inactiveProduct.setName("Inativo");
        inactiveProduct.setCostPrice(new BigDecimal("10.00"));
        inactiveProduct.setSalePrice(new BigDecimal("20.00"));
        inactiveProduct.setStock(0);
        inactiveProduct.setActive(false);

        when(productRepository.findAllByActive(false))
                .thenReturn(List.of(inactiveProduct));

        List<ProductDTO> result = productService.listProducts(false, false);

        assertEquals(1, result.size());
        assertFalse(result.get(0).active());
        verify(productRepository).findAllByActive(false);
        verify(productRepository, never()).findAllByActiveTrue();
        verify(productRepository, never()).findAll();
    }

    @Test
    void shouldDeactivateProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Produto");
        product.setCostPrice(new BigDecimal("10.00"));
        product.setSalePrice(new BigDecimal("20.00"));
        product.setStock(1);
        product.setActive(true);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO result = productService.deactivateProduct(productId);

        assertFalse(result.active());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldActivateProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Produto");
        product.setCostPrice(new BigDecimal("10.00"));
        product.setSalePrice(new BigDecimal("20.00"));
        product.setStock(1);
        product.setActive(false);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO result = productService.activateProduct(productId);

        assertTrue(result.active());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdate(){

        Long productId = 1L;

        UpdateProductDTO updateProduct = new UpdateProductDTO(
                "Base Líquida Matte",
                new BigDecimal("80.00"),
                new BigDecimal("130.00"),
                20
        );
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());



        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
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
                new BigDecimal("200.00"),
                15
        );

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Mouse Gamer");
        existingProduct.setCostPrice(new BigDecimal("100.00"));
        existingProduct.setStock(10);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        CostPriceCannotBeNegativeException exception = assertThrows(
                CostPriceCannotBeNegativeException.class,
                        () -> productService.updateProduct(productId, product)
        );

        assertEquals("Cost Price cannot be negative", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    void shouldThrowExceptionWhenProductNotFoundOnDelete() {

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(productId)
        );

        verify(productRepository).findById(productId);
        verify(productRepository, never()).delete(any());
    }

        @Test
        void shouldThrowProductInUseWhenDeleteReferencedBySaleItems() {

                Long productId = 2L;

                Product product = new Product();
                product.setId(productId);
                product.setName("Produto vendido");
                product.setCostPrice(new BigDecimal("10.00"));
                product.setStock(0);

                when(productRepository.findById(productId))
                                .thenReturn(Optional.of(product));

                doThrow(new DataIntegrityViolationException("fk_sale_items_product"))
                                .when(productRepository)
                                .delete(product);

                ProductInUseException exception = assertThrows(
                                ProductInUseException.class,
                                () -> productService.deleteProduct(productId)
                );

                assertTrue(exception.getMessage().contains("Product cannot be deleted"));

                verify(productRepository).findById(productId);
                verify(productRepository).delete(product);
        }

}