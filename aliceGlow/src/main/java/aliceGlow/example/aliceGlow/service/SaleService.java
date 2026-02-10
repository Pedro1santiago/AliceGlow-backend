package aliceGlow.example.aliceGlow.service;
import aliceGlow.example.aliceGlow.domain.Product;
import aliceGlow.example.aliceGlow.domain.Sale;
import aliceGlow.example.aliceGlow.domain.SaleItem;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.ProductSalesDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleItemRepository;
import aliceGlow.example.aliceGlow.repository.SaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private static final Logger log = LoggerFactory.getLogger(SaleService.class);
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, SaleItemRepository saleItemRepository){
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleDTO> listSales(){
      return saleRepository.findAll().stream().map(SaleDTO::toDTO).toList();
    }

    public SaleDTO sale(CreateSaleDTO createSaleDTO) {

        Sale sale = new Sale();
        sale.setClient(createSaleDTO.client());

        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateSaleItemDTO itemDTO : createSaleDTO.saleItems()) {

            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal unitPrice = product.getCostPrice();
            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(itemDTO.quantity())
            );

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);

            items.add(item);
            total = total.add(subtotal);
        }

        sale.setItems(items);
        sale.setTotal(total);

        Sale savedSale = saleRepository.save(sale);

        return SaleDTO.toDTO(savedSale);
    }

    public void deleteSale(Long id){
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        saleRepository.delete(sale);
    }

    public BigDecimal invoicing(){
        return saleRepository.sumAllInvoicing();

    }

    public BigDecimal invoicingByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleRepository.sumInvoicingByPeriod(start, end);
    }

    public BigDecimal profit(){
        return saleItemRepository.calculateTotalProfit();
    }

    public List<ProductSalesDTO> listProductSales() {

        return saleItemRepository.findTopSellingProducts()
                .stream()
                .map(row -> new ProductSalesDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }
}
