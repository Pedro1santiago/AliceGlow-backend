package aliceGlow.example.aliceGlow.dto.saleItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateSaleItemDTO(

        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Integer quantity
) {}
