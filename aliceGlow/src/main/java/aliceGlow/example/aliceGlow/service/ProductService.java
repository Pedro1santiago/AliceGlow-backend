package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductMarginDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.exception.CostPriceCannotBeNegativeException;
import aliceGlow.example.aliceGlow.exception.ProductInUseException;
import aliceGlow.example.aliceGlow.exception.ProductNotFoundException;
import aliceGlow.example.aliceGlow.exception.SalePriceCannotBeNegativeException;
import aliceGlow.example.aliceGlow.exception.StockNegativeException;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> listProducts(Boolean active, boolean includeInactive){
        List<Product> products;

        if (includeInactive) {
            products = productRepository.findAll();
        } else if (active == null) {
            products = productRepository.findAllByActiveTrue();
        } else {
            products = productRepository.findAllByActive(active);
        }

        return products.stream().map(ProductDTO::toDTO).toList();
    }

    public Page<ProductDTO> listProductsPage(Boolean active, boolean includeInactive, Pageable pageable) {
        if (includeInactive) {
            return productRepository.findAll(pageable).map(ProductDTO::toDTO);
        }

        if (active == null) {
            return productRepository.findAllByActiveTrue(pageable).map(ProductDTO::toDTO);
        }

        return productRepository.findAllByActive(active, pageable).map(ProductDTO::toDTO);
    }

        public List<ProductMarginDTO> listProductMargins() {
        return productRepository.findAll()
            .stream()
            .map(product -> {
                BigDecimal costPrice = product.getCostPrice();
                BigDecimal salePrice = product.getSalePrice();

                if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
                return new ProductMarginDTO(
                    product.getId(),
                    product.getName(),
                    costPrice,
                    salePrice,
                    null,
                    null
                );
                }

                BigDecimal marginAmount = salePrice.subtract(costPrice);
                BigDecimal marginPercent = marginAmount
                    .divide(salePrice, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);

                return new ProductMarginDTO(
                    product.getId(),
                    product.getName(),
                    costPrice,
                    salePrice,
                    marginAmount,
                    marginPercent
                );
            })
            .toList();
        }

    public ProductDTO createProduct(CreateProductDTO createProductDTO){
        Product product = new Product();
        product.setName(createProductDTO.name());
        product.setCostPrice(createProductDTO.costPrice());
        product.setSalePrice(createProductDTO.salePrice());
        product.setStock(createProductDTO.stock());
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        return ProductDTO.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (updateProductDTO.name() != null) {
            product.setName(updateProductDTO.name());
        }

        if (updateProductDTO.costPrice() != null) {
            if (updateProductDTO.costPrice().compareTo(BigDecimal.ZERO) < 0 ){
                throw new CostPriceCannotBeNegativeException();
            }
            product.setCostPrice(updateProductDTO.costPrice());
        }

        if (updateProductDTO.salePrice() != null) {
            if (updateProductDTO.salePrice().compareTo(BigDecimal.ZERO) < 0 ){
                throw new SalePriceCannotBeNegativeException();
            }
            product.setSalePrice(updateProductDTO.salePrice());
        }

        if (updateProductDTO.stock() != null) {
            if (updateProductDTO.stock() < 0) {
                throw new StockNegativeException();
            }
            product.setStock(updateProductDTO.stock());
        }

        Product updatedProduct = productRepository.save(product);

        return ProductDTO.toDTO(updatedProduct);
    }

    public ProductDTO activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.setActive(true);
        return ProductDTO.toDTO(productRepository.save(product));
    }

    public ProductDTO deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.setActive(false);
        return ProductDTO.toDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        try {
            productRepository.delete(product);
        } catch (DataIntegrityViolationException ex) {
            throw new ProductInUseException(id);
        }
    }
}
