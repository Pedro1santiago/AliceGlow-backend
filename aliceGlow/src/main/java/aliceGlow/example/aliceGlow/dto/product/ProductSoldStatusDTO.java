package aliceGlow.example.aliceGlow.dto.product;

public record ProductSoldStatusDTO(
        Long productId,
        String name,
        Long soldQuantity,
        boolean sold
) {}
