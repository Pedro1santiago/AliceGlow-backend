package aliceGlow.example.aliceGlow.dto.stockPurchase;

import aliceGlow.example.aliceGlow.domain.StockPurchase;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StockPurchaseDTO(
        Long id,
        LocalDate purchaseDate,
        String description,
        LocalDateTime createdAt
) {

    public static StockPurchaseDTO toDTO(StockPurchase purchase) {
        return new StockPurchaseDTO(
                purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getDescription(),
                purchase.getCreatedAt()
        );
    }
}
