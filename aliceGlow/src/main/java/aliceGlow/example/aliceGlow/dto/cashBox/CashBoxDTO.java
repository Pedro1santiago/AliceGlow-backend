package aliceGlow.example.aliceGlow.dto.cashBox;

import aliceGlow.example.aliceGlow.domain.CashBox;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CashBoxDTO(
        Long id,
        LocalDate businessDate,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CashOutflowDTO> outflows
) {

    public static CashBoxDTO toDTO(CashBox cashBox) {
        List<CashOutflowDTO> outflows = cashBox.getOutflows() == null
                ? List.of()
                : cashBox.getOutflows().stream().map(CashOutflowDTO::toDTO).toList();

        return new CashBoxDTO(
                cashBox.getId(),
                cashBox.getBusinessDate(),
                cashBox.getBalance(),
                cashBox.getCreatedAt(),
                cashBox.getUpdatedAt(),
                outflows
        );
    }
}
