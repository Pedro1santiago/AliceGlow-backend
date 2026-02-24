package aliceGlow.example.aliceGlow.dto.stockPurchase;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateStockPurchaseDTO(
        @NotNull
        LocalDate purchaseDate,
        String description
) {}
