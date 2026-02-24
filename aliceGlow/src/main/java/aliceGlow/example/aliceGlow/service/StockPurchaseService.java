package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.StockPurchase;
import aliceGlow.example.aliceGlow.dto.stockPurchase.CreateStockPurchaseDTO;
import aliceGlow.example.aliceGlow.dto.stockPurchase.StockPurchaseDTO;
import aliceGlow.example.aliceGlow.repository.StockPurchaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockPurchaseService {

    private final StockPurchaseRepository stockPurchaseRepository;

    public StockPurchaseService(StockPurchaseRepository stockPurchaseRepository) {
        this.stockPurchaseRepository = stockPurchaseRepository;
    }

    public StockPurchaseDTO create(CreateStockPurchaseDTO dto) {
        StockPurchase purchase = new StockPurchase();
        purchase.setPurchaseDate(dto.purchaseDate());
        purchase.setDescription(dto.description());
        return StockPurchaseDTO.toDTO(stockPurchaseRepository.save(purchase));
    }

    public List<StockPurchaseDTO> listByPeriod(LocalDate start, LocalDate end) {
        return stockPurchaseRepository.findAllByPurchaseDateBetween(start, end)
                .stream()
                .map(StockPurchaseDTO::toDTO)
                .toList();
    }
}
