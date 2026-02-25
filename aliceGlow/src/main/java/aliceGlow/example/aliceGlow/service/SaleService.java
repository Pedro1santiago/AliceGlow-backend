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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Lists all sales.
     */
    @Transactional(readOnly = true)
    public List<SaleDTO> listSales() {
        return saleRepository.findAllWithItems()
                .stream()
                .map(SaleDTO::toDTO)
                .toList();
    }

    /**
     * Lists sales with pagination.
     */
    @Transactional(readOnly = true)
    public Page<SaleDTO> listSalesPage(Pageable pageable) {
        return saleRepository.findAll(pageable).map(SaleDTO::toDTO);
    }

    /**
     * Lists sales filtered by creation date.
     */
    @Transactional(readOnly = true)
    public List<SaleDTO> listSalesByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findAllByCreatedAtBetweenWithItems(start, end)
                .stream()
                .map(SaleDTO::toDTO)
                .toList();
    }

    /**
     * Lists sales with pagination filtered by creation date.
     */
    @Transactional(readOnly = true)
    public Page<SaleDTO> listSalesByPeriodPage(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return saleRepository.findAllByCreatedAtBetween(start, end, pageable).map(SaleDTO::toDTO);
    }

    /**
     * Retrieves a sale by id.
     */
    @Transactional(readOnly = true)
    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findByIdWithItems(id)
                .orElseThrow(SaleNotFoundException::new);
        return SaleDTO.toDTO(sale);
    }

    /**
     * Creates a sale (PENDING), validates items, and decrements product stock.
     */
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

    /**
     * Cancels the sale and clears the paid date.
     */
    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(SaleNotFoundException::new);

        sale.setStatus(SaleStatus.CANCELED);
        sale.setPaidAt(null);

        saleRepository.save(sale);
    }

    /**
     * Marks the sale as paid and sets the paid timestamp.
     */
    public void markAsPaid(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(SaleNotFoundException::new);

        sale.setStatus(SaleStatus.PAID);
        sale.setPaidAt(LocalDateTime.now());

        saleRepository.save(sale);
    }

    /**
     * Sums total sales invoicing.
     */
    public BigDecimal invoicing() {
        return saleRepository.sumAllInvoicing();
    }

    /**
     * Sums invoicing for the given period.
     */
    public BigDecimal invoicingByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleRepository.sumInvoicingByPeriod(start, end);
    }

    /**
     * Calculates total profit based on the cost snapshot of sold items.
     */
    public BigDecimal profit() {
        return saleItemRepository.calculateTotalProfit();
    }

    /**
     * Calculates total profit for the given period.
     */
    public BigDecimal profitByPeriod(LocalDateTime start, LocalDateTime end) {
        return saleItemRepository.calculateTotalProfitByPeriod(start, end);
    }

    /**
     * Returns the top-selling products (name + quantity).
     */
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