package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.ProductSalesDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final SaleService saleService;

    public ReportController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/invoicing")
    public ResponseEntity<BigDecimal> invoicing() {
        return ResponseEntity.ok(saleService.invoicing());
    }

    @GetMapping("/invoicing/period")
    public ResponseEntity<BigDecimal> invoicingByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(saleService.invoicingByPeriod(start, end));
    }

    @GetMapping("/profit")
    public ResponseEntity<BigDecimal> profit() {
        return ResponseEntity.ok(saleService.profit());
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<ProductSalesDTO>> topProducts() {
        return ResponseEntity.ok(saleService.listProductSales());
    }
}