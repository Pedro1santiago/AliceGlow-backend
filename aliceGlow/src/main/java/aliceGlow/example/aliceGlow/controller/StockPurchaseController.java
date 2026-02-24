package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.stockPurchase.CreateStockPurchaseDTO;
import aliceGlow.example.aliceGlow.dto.stockPurchase.StockPurchaseDTO;
import aliceGlow.example.aliceGlow.service.StockPurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/stock-purchases")
public class StockPurchaseController {

    private final StockPurchaseService stockPurchaseService;

    public StockPurchaseController(StockPurchaseService stockPurchaseService) {
        this.stockPurchaseService = stockPurchaseService;
    }

    @PostMapping
    public ResponseEntity<StockPurchaseDTO> create(@Valid @RequestBody CreateStockPurchaseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockPurchaseService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<StockPurchaseDTO>> list(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        LocalDate s;
        LocalDate e;

        if (start != null && end != null) {
            s = start;
            e = end;
        } else if (month != null && year != null) {
            YearMonth ym = YearMonth.of(year, month);
            s = ym.atDay(1);
            e = ym.atEndOfMonth();
        } else {
            YearMonth ym = YearMonth.now();
            s = ym.atDay(1);
            e = ym.atEndOfMonth();
        }

        return ResponseEntity.ok(stockPurchaseService.listByPeriod(s, e));
    }
}
