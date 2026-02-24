package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.ProductSalesDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductMarginDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductSalesStatusSummaryDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductSoldStatusDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowReportDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
import aliceGlow.example.aliceGlow.service.ReportQueryPeriod;
import aliceGlow.example.aliceGlow.service.ReportService;
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
    private final ProductService productService;
    private final ReportService reportService;

    public ReportController(SaleService saleService, ProductService productService, ReportService reportService) {
        this.saleService = saleService;
        this.productService = productService;
        this.reportService = reportService;
    }

    @GetMapping("/invoicing")
    public ResponseEntity<BigDecimal> invoicing() {
        return ResponseEntity.ok(saleService.invoicing());
    }

    @GetMapping("/invoicing/period")
    public ResponseEntity<BigDecimal> invoicingByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        ReportQueryPeriod period = ReportQueryPeriod.from(start, end, month, year);
        return ResponseEntity.ok(saleService.invoicingByPeriod(period.start(), period.end()));
    }

    @GetMapping("/profit")
    public ResponseEntity<BigDecimal> profit() {
        return ResponseEntity.ok(saleService.profit());
    }

    @GetMapping("/profit/period")
    public ResponseEntity<BigDecimal> profitByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        ReportQueryPeriod period = ReportQueryPeriod.from(start, end, month, year);
        return ResponseEntity.ok(saleService.profitByPeriod(period.start(), period.end()));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<ProductSalesDTO>> topProducts() {
        return ResponseEntity.ok(saleService.listProductSales());
    }

    @GetMapping("/product-margins")
    public ResponseEntity<List<ProductMarginDTO>> productMargins() {
        return ResponseEntity.ok(productService.listProductMargins());
    }

    @GetMapping("/products/sales-status")
    public ResponseEntity<List<ProductSoldStatusDTO>> productsSalesStatus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        ReportQueryPeriod period = ReportQueryPeriod.from(start, end, month, year);
        return ResponseEntity.ok(reportService.productsSoldStatus(period.start(), period.end()));
    }

    @GetMapping("/products/sales-status/summary")
    public ResponseEntity<ProductSalesStatusSummaryDTO> productsSalesStatusSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        ReportQueryPeriod period = ReportQueryPeriod.from(start, end, month, year);
        return ResponseEntity.ok(reportService.productsSoldStatusSummary(period.start(), period.end()));
    }

    @GetMapping("/cash-outflows")
    public ResponseEntity<CashOutflowReportDTO> cashOutflows(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        ReportQueryPeriod period = ReportQueryPeriod.from(start, end, month, year);
        return ResponseEntity.ok(reportService.cashOutflows(period.start(), period.end()));
    }
}