package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * Lists sales. If start/end are provided, applies a period filter.
     */
    @GetMapping
    public ResponseEntity<List<SaleDTO>> listSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        if (start == null && end == null) {
            return ResponseEntity.ok(saleService.listSales());
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Both start and end must be provided");
        }
        return ResponseEntity.ok(saleService.listSalesByPeriod(start, end));
    }

    /**
     * Lists sales with pagination. If start/end are provided, applies a period filter.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<SaleDTO>> listSalesPage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (start == null && end == null) {
            return ResponseEntity.ok(saleService.listSalesPage(pageable));
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Both start and end must be provided");
        }
        return ResponseEntity.ok(saleService.listSalesByPeriodPage(start, end, pageable));
    }

    /**
     * Retrieves a sale by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    /**
     * Creates a new sale and decrements stock for each sold item.
     */
    @PostMapping
    public ResponseEntity<SaleDTO> create(@Valid @RequestBody CreateSaleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.sale(dto));
    }

    /**
     * Cancels a sale.
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        saleService.cancelSale(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Marks a sale as paid.
     */
    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        saleService.markAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}