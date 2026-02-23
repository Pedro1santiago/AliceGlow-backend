package aliceGlow.example.aliceGlow.dto.product;

import aliceGlow.example.aliceGlow.domain.Product;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, BigDecimal costPrice, BigDecimal salePrice, Integer stock) {

    public static ProductDTO toDTO(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getCostPrice(), product.getSalePrice(), product.getStock());
    }

}
