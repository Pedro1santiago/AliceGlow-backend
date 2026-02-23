package aliceGlow.example.aliceGlow.dto.saleItem;

import aliceGlow.example.aliceGlow.domain.SaleItem;

import java.math.BigDecimal;

public record SaleItemDTO(
    Long id,
    Long saleId,
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal unitCostPrice,
    BigDecimal subtotal,
    BigDecimal costSubtotal
) {

    public static SaleItemDTO toDTO(SaleItem saleItem) {
        return new SaleItemDTO(
                saleItem.getId(),
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getProduct().getName(),
                saleItem.getQuantity(),
                saleItem.getUnitPrice(),
                saleItem.getUnitCostPrice(),
                saleItem.getSubtotal(),
                saleItem.getCostSubtotal()
        );
    }
}
