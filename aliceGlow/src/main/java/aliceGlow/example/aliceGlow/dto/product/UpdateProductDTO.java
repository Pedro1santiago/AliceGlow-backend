package aliceGlow.example.aliceGlow.dto.product;
import java.math.BigDecimal;

public record UpdateProductDTO(

        String name,

        BigDecimal costPrice,

        Integer stock
){}
