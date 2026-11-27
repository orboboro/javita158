package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/** La chiave di un dettaglio è la coppia (ordine, prodotto). Non è un'entity. */
@Embeddable
public class OrderDetailId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "orderid", nullable = false)
    private Integer orderId;

    @Column(name = "productid", nullable = false)
    private Integer productId;

    public OrderDetailId() {
    }

    public OrderDetailId(Integer orderId, Integer productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderDetailId that)) {
            return false;
        }
        return Objects.equals(orderId, that.orderId)
                && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
