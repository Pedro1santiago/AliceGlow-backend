package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowReportDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductSoldStatusDTO;
import aliceGlow.example.aliceGlow.repository.CashOutflowRepository;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;
    private final CashOutflowRepository cashOutflowRepository;

    public ReportService(
            ProductRepository productRepository,
            SaleItemRepository saleItemRepository,
            CashOutflowRepository cashOutflowRepository
    ) {
        this.productRepository = productRepository;
        this.saleItemRepository = saleItemRepository;
        this.cashOutflowRepository = cashOutflowRepository;
    }

    public List<ProductSoldStatusDTO> productsSoldStatus(LocalDateTime start, LocalDateTime end) {
        Map<Long, Long> soldQuantityByProductId = new HashMap<>();

        for (Object[] row : saleItemRepository.sumSoldQuantityByProductIdBetween(start, end)) {
            Long productId = (Long) row[0];
            Long quantity = ((Number) row[1]).longValue();
            soldQuantityByProductId.put(productId, quantity);
        }

        return productRepository.findAll()
                .stream()
                .map(product -> toSoldStatus(product, soldQuantityByProductId))
                .toList();
    }

    private ProductSoldStatusDTO toSoldStatus(Product product, Map<Long, Long> soldQuantityByProductId) {
        Long quantity = soldQuantityByProductId.getOrDefault(product.getId(), 0L);
        return new ProductSoldStatusDTO(product.getId(), product.getName(), quantity, quantity > 0);
    }

    public CashOutflowReportDTO cashOutflows(LocalDateTime start, LocalDateTime end) {
        List<CashOutflowDTO> outflows = cashOutflowRepository.findAllByOccurredAtBetween(start, end)
                .stream()
                .map(CashOutflowDTO::toDTO)
                .toList();

        BigDecimal total = cashOutflowRepository.sumAmountByOccurredAtBetween(start, end);
        if (total == null) {
            total = BigDecimal.ZERO;
        }

        return new CashOutflowReportDTO(total, outflows);
    }
}
