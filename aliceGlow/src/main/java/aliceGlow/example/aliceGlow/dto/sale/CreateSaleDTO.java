package aliceGlow.example.aliceGlow.dto.sale;

import aliceGlow.example.aliceGlow.domain.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;

public record CreateSaleDTO(

        @NotBlank
        String client,

        @NotNull
        PaymentMethod paymentMethod,

        @NotNull
        List<CreateSaleItemDTO> saleItems
) {}