package aliceGlow.example.aliceGlow.dto.cashOutflow;

import aliceGlow.example.aliceGlow.domain.CashOutflow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashOutflowDTO(
        Long id,
        Long cashBoxId,
        String description,
        BigDecimal amount,
        LocalDateTime occurredAt,
        LocalDateTime createdAt
) {

    public static CashOutflowDTO toDTO(CashOutflow outflow) {
        return new CashOutflowDTO(
                outflow.getId(),
                outflow.getCashBox().getId(),
                outflow.getDescription(),
                outflow.getAmount(),
                outflow.getOccurredAt(),
                outflow.getCreatedAt()
        );
    }
}
