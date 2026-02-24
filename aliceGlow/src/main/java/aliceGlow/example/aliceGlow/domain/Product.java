package aliceGlow.example.aliceGlow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Boolean active = true;

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public BigDecimal getCostPrice(){return costPrice;}
    public void setCostPrice(BigDecimal costPrice){this.costPrice = costPrice;}

    public BigDecimal getSalePrice(){return salePrice;}
    public void setSalePrice(BigDecimal salePrice){this.salePrice = salePrice;}

    public Integer getStock(){return stock;}
    public void setStock(Integer stock){this.stock = stock;}

    public Boolean getActive() {return active;}
    public void setActive(Boolean active) {this.active = active;}
}
