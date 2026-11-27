package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Entity della tabella products del database company. */
@Entity
@Table(name = "products", schema = "public")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_generator")
    @SequenceGenerator(name = "products_id_generator", sequenceName = "products_productid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @Column(name = "productname", nullable = false, length = 40)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplierid", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryid", nullable = false)
    private Category category;

    @Column(name = "unitprice", nullable = false, columnDefinition = "numeric")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "discontinued", nullable = false)
    private Integer discontinued = 0;

    public Product() {
    }

    public Product(
            Integer id,
            String productName,
            Supplier supplier,
            Category category,
            BigDecimal unitPrice,
            Integer discontinued) {
        this.id = id;
        this.productName = productName;
        this.supplier = supplier;
        this.category = category;
        this.unitPrice = unitPrice;
        this.discontinued = discontinued;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(Integer discontinued) {
        this.discontinued = discontinued;
    }
}
