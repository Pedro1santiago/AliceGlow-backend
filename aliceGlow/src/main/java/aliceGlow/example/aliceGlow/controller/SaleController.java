package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.ProductSalesDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService){
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleDTO> listSales(){
        return saleService.listSales();
    }

    @PostMapping
    public SaleDTO createSale(@RequestBody CreateSaleDTO createSaleDTO){
        return saleService.sale(createSaleDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id){
        saleService.deleteSale(id);
    }


    @GetMapping("/reports/invoicing")
    public BigDecimal invoicing(){
        return saleService.invoicing();
    }

    @GetMapping("/reports/invoicing/period")
    public BigDecimal invoicingByPeriod(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ){
        return saleService.invoicingByPeriod(start, end);
    }

    @GetMapping("/reports/profit")
    public BigDecimal profit(){
        return saleService.profit();
    }

    @GetMapping("/reports/best-sales")
    public List<ProductSalesDTO> listProductSales(){
        return saleService.listProductSales();
    }
}

