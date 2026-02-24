package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.*;
import aliceGlow.example.aliceGlow.dto.sale.CreateSaleDTO;
import aliceGlow.example.aliceGlow.dto.sale.ProductSalesDTO;
import aliceGlow.example.aliceGlow.dto.sale.SaleDTO;
import aliceGlow.example.aliceGlow.dto.saleItem.CreateSaleItemDTO;
import aliceGlow.example.aliceGlow.exception.InsufficientStockException;
import aliceGlow.example.aliceGlow.exception.ProductNotFoundException;
import aliceGlow.example.aliceGlow.exception.SaleNotFoundException;
import aliceGlow.example.aliceGlow.exception.SaleWithoutItemsException;
import aliceGlow.example.aliceGlow.repository.ProductRepository;
import aliceGlow.example.aliceGlow.repository.SaleItemRepository;
import aliceGlow.example.aliceGlow.repository.SaleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;

    public SaleService(
            SaleRepository saleRepository,
            ProductRepository productRepository,
            SaleItemRepository saleItemRepository
    ) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleDTO> listSales() {
        return saleRepository.findAll()
                .stream()
                .map(SaleDTO::toDTO)
                .toList();
    }

    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(SaleNotFoundException::new);
        return SaleDTO.toDTO(sale);
    }

    @Transactional
    public SaleDTO sale(CreateSaleDTO dto) {

        if (dto.saleItems() == null || dto.saleItems().isEmpty()) {
            throw new SaleWithoutItemsException();
        }

        Sale sale = new Sale();
        sale.setClient(dto.client());
        sale.setPaymentMethod(dto.paymentMethod());
        sale.setStatus(SaleStatus.PENDING);
        sale.setPaidAt(null);

        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateSaleItemDTO itemDTO : dto.saleItems()) {

            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(ProductNotFoundException::new);

            if (product.getStock() < itemDTO.quantity()) {
                throw new InsufficientStockException(product.getId());
            }

                BigDecimal quantity = BigDecimal.valueOf(itemDTO.quantity());
                BigDecimal unitCostPrice = product.getCostPrice();
                BigDecimal unitPrice = itemDTO.unitPrice();
                BigDecimal subtotal = unitPrice.multiply(quantity);
                BigDecimal costSubtotal = unitCostPrice.multiply(quantity);

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
                item.setUnitPrice(unitPrice);
                item.setUnitCostPrice(unitCostPrice);
            item.setSubtotal(subtotal);
                item.setCostSubtotal(costSubtotal);

            items.add(item);
            total = total.add(subtotal);

            product.setStock(product.getStock() - itemDTO.quantity());
        }

        sale.setItems(items);
        sale.setTotal(total);

        return SaleDTO.toDTO(saleRepository.save(sale));
    }

    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(SaleNotFoundException::new);

        sale.setStatus(SaleStatus.CANCELED);
        sale.setPaidAt(null);

        saleRepository.save(sale);
    }

    public void markAsPaid(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(SaleNotFoundException::new);

        sale.setStatus(SaleStatus.PAID);
        sale.setPaidAt(LocalDateTime.now());

        saleRepository.save(sale);
    }

    public BigDecimal invoicing() {
        return saleRepository.sumAllInvoicing();
    }

    public BigDecimal invoicingByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleRepository.sumInvoicingByPeriod(start, end);
    }

    public BigDecimal profit() {
        return saleItemRepository.calculateTotalProfit();
    }

    public BigDecimal profitByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleItemRepository.calculateTotalProfitByPeriod(start, end);
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