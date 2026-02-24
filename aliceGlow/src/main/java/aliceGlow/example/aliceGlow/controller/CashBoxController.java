package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.cashBox.CashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.CreateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.UpdateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CreateCashOutflowDTO;
import aliceGlow.example.aliceGlow.service.CashBoxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cash-boxes")
public class CashBoxController {

    private final CashBoxService cashBoxService;

    public CashBoxController(CashBoxService cashBoxService) {
        this.cashBoxService = cashBoxService;
    }

    @PostMapping
    public ResponseEntity<CashBoxDTO> create(@Valid @RequestBody CreateCashBoxDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cashBoxService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashBoxDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cashBoxService.findById(id));
    }

    @GetMapping
    public ResponseEntity<CashBoxDTO> findByBusinessDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(cashBoxService.findByBusinessDate(date));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CashBoxDTO> updateBalance(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCashBoxDTO dto
    ) {
        return ResponseEntity.ok(cashBoxService.updateBalance(id, dto));
    }

    @PostMapping("/{id}/outflows")
    public ResponseEntity<CashOutflowDTO> addOutflow(
            @PathVariable Long id,
            @Valid @RequestBody CreateCashOutflowDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cashBoxService.addOutflow(id, dto));
    }
}
