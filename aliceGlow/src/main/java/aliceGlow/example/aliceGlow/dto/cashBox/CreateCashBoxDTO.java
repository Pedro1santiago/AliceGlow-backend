package aliceGlow.example.aliceGlow.dto.cashBox;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCashBoxDTO(
        @NotNull
        LocalDate businessDate,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal balance
) {}
