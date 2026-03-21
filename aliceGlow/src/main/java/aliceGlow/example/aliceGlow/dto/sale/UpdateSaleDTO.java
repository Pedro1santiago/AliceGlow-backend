package aliceGlow.example.aliceGlow.dto.sale;

import aliceGlow.example.aliceGlow.domain.PaymentMethod;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateSaleDTO(
        @NotBlank
        String client,

        /**
         * Optional sale date/time (Brazil local). If omitted, keeps the current sale date.
         */
        LocalDateTime saleDate,

        @NotNull
        PaymentMethod paymentMethod,

        @NotNull
        List<CreateSaleItemDTO> saleItems
) {
}
