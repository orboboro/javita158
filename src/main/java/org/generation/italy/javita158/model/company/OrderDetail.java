package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Entity della tabella orderdetails: la chiave deriva dall'ordine e dal prodotto. */
@Entity
@Table(name = "orderdetails", schema = "public")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id = new OrderDetailId();

    // MapsId indica quale attributo della chiave viene ricavato dalla relazione.
    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderid", nullable = false)
    private Order order;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @Column(name = "unitprice", nullable = false, columnDefinition = "numeric")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "qty", nullable = false)
    private Integer quantity = 1;

    @Column(name = "discount", nullable = false, precision = 4, scale = 3)
    private BigDecimal discount = BigDecimal.ZERO;

    public OrderDetail() {
    }

    // Hibernate completa l'id usando le relazioni quando rende persistente il dettaglio.
    public OrderDetail(Order order, Product product, BigDecimal unitPrice,
                       Integer quantity, BigDecimal discount) {
        this.order = order;
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public OrderDetailId getId() {
        return id;
    }

    public void setId(OrderDetailId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
