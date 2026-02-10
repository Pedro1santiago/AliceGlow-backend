package aliceGlow.example.aliceGlow.dto.sale;

import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateSaleDTO(

        @NotBlank
        String client,

        @NotNull
        List<CreateSaleItemDTO> saleItems
) {}
