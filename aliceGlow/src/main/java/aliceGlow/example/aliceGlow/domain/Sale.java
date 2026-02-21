package aliceGlow.example.aliceGlow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private boolean canceled = false;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    public boolean isCanceled() { return canceled; }
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
}