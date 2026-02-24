package aliceGlow.example.aliceGlow.dto.cashOutflow;

import java.math.BigDecimal;
import java.util.List;

public record CashOutflowReportDTO(
        BigDecimal total,
        List<CashOutflowDTO> outflows
) {}
