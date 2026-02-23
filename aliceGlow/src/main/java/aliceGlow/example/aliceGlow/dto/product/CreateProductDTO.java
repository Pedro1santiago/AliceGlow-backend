package aliceGlow.example.aliceGlow.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductDTO(
        @NotBlank
        String name,

        @DecimalMin("0.01")
        BigDecimal costPrice,

        @DecimalMin("0.01")
        BigDecimal salePrice,

        @NotNull
        @Min(0)
        Integer stock){}
