package aliceGlow.example.aliceGlow.dto.sale;

import aliceGlow.example.aliceGlow.domain.Sale;
import aliceGlow.example.aliceGlow.domain.SaleStatus;
import aliceGlow.example.aliceGlow.domain.PaymentMethod;
import aliceGlow.example.aliceGlow.dto.saleItem.SaleItemDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleDTO(
        Long id,
        LocalDateTime createdAt,
        String client,
        BigDecimal total,
        SaleStatus status,
        PaymentMethod paymentMethod,
        LocalDateTime paidAt,
        List<SaleItemDTO> saleItems
) {

    public static SaleDTO toDTO(Sale sale){
        return new SaleDTO(
                sale.getId(),
                sale.getCreatedAt(),
                sale.getClient(),
                sale.getTotal(),
                sale.getStatus(),
                sale.getPaymentMethod(),
                sale.getPaidAt(),
                sale.getItems().stream().map(SaleItemDTO::toDTO).toList()
        );
    }
}