package aliceGlow.example.aliceGlow.dto.cashBox;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateCashBoxDTO(
        @NotNull
        @DecimalMin("0.00")
        BigDecimal balance
) {}
