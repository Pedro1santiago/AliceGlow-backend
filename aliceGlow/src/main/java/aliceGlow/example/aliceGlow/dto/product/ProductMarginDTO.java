package aliceGlow.example.aliceGlow.dto.product;

import java.math.BigDecimal;

public record ProductMarginDTO(
        Long id,
        String name,
        BigDecimal costPrice,
        BigDecimal salePrice,
        BigDecimal marginAmount,
        BigDecimal marginPercent
) {}
