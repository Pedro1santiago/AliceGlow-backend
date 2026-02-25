package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.cashBox.CashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.CreateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.UpdateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CreateCashOutflowDTO;
import aliceGlow.example.aliceGlow.service.CashBoxService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cash-boxes")
public class CashBoxController {

    private final CashBoxService cashBoxService;

    public CashBoxController(CashBoxService cashBoxService) {
        this.cashBoxService = cashBoxService;
    }

    /**
     * Creates a cash box for a business date (businessDate).
     */
    @PostMapping
    public ResponseEntity<CashBoxDTO> create(@Valid @RequestBody CreateCashBoxDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cashBoxService.create(dto));
    }

    /**
     * Retrieves a cash box by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CashBoxDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cashBoxService.findById(id));
    }

    /**
     * Lists all cash boxes (sorted by date descending).
     */
    @GetMapping
    public ResponseEntity<List<CashBoxDTO>> listAll() {
        return ResponseEntity.ok(cashBoxService.listAll());
    }

    /**
     * Lists cash boxes with pagination (sorted by date descending).
     */
    @GetMapping("/page")
    public ResponseEntity<Page<CashBoxDTO>> listAllPage(
            @PageableDefault(size = 20, sort = "businessDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cashBoxService.listAllPage(pageable));
    }

    /**
     * Retrieves a cash box by business date.
     */
    @GetMapping(params = "date")
    public ResponseEntity<CashBoxDTO> findByBusinessDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(cashBoxService.findByBusinessDate(date));
    }

    /**
     * Updates the cash box balance.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CashBoxDTO> updateBalance(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCashBoxDTO dto
    ) {
        return ResponseEntity.ok(cashBoxService.updateBalance(id, dto));
    }

    /**
     * Registers an outflow and updates the cash box balance.
     */
    @PostMapping("/{id}/outflows")
    public ResponseEntity<CashOutflowDTO> addOutflow(
            @PathVariable Long id,
            @Valid @RequestBody CreateCashOutflowDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cashBoxService.addOutflow(id, dto));
    }
}
