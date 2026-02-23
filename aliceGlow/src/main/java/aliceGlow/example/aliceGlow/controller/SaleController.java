package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> listSales() {
        return ResponseEntity.ok(saleService.listSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> create(@Valid @RequestBody CreateSaleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.sale(dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        saleService.cancelSale(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        saleService.markAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}