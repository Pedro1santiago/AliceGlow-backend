package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> listProducts(){
        return productRepository.findAll()
                .stream()
                .map(ProductDTO::toDTO)
                .toList();
    }

    public ProductDTO createProduct(CreateProductDTO createProductDTO){
        Product product = new Product();
        product.setName(createProductDTO.name());
        product.setCostPrice(createProductDTO.costPrice());
        product.setStock(createProductDTO.stock());

        Product savedProduct = productRepository.save(product);

        return ProductDTO.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (updateProductDTO.name() != null) {
            product.setName(updateProductDTO.name());
        }

        if (updateProductDTO.costPrice() != null) {
            if (updateProductDTO.costPrice().compareTo(BigDecimal.ZERO) < 0 ){
                throw new RuntimeException("CostPrice cannot be negative");
            }
            product.setCostPrice(updateProductDTO.costPrice());
        }

        if (updateProductDTO.stock() != null) {
            if (updateProductDTO.stock() < 0) {
                throw new IllegalArgumentException("Stock cannot be negative");
            }
            product.setStock(updateProductDTO.stock());
        }

        Product updatedProduct = productRepository.save(product);

        return ProductDTO.toDTO(updatedProduct);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }
}
